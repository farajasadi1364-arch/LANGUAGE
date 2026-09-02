package com.languageos.app.data.repository

import com.google.gson.Gson
import com.languageos.app.data.local.SyncQueueDao
import com.languageos.app.data.local.SyncQueueEntity
import com.languageos.app.data.local.VocabularyDao
import com.languageos.app.data.local.VocabularyItemEntity
import com.languageos.app.data.remote.ApiService
import com.languageos.app.data.remote.VocabularyReviewRequest
import com.languageos.app.domain.SpacedRepetitionEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyRepository @Inject constructor(
    private val vocabularyDao: VocabularyDao,
    private val syncQueueDao: SyncQueueDao,
    private val apiService: ApiService,
    private val gson: Gson,
) {

    suspend fun getDueItems(limit: Int = 20): List<VocabularyItemEntity> =
        vocabularyDao.getDueForReview(System.currentTimeMillis(), limit)

    /**
     * Offline-first طبق بخش ۷ و ۱۴ PRD: همیشه اول Local ذخیره می‌شود (پس UI فوری واکنش نشون می‌ده)،
     * بعد تلاش برای ارسال فوری به سرور؛ اگر شکست خورد (بدون اینترنت)، به Sync Queue اضافه می‌شود
     * تا SyncWorker با اتصال بعدی دوباره تلاش کند.
     */
    suspend fun submitReview(itemId: String, wasCorrect: Boolean) {
        val now = System.currentTimeMillis()
        val current = vocabularyDao.getReviewState(itemId)
            ?: SpacedRepetitionEngine.initialState(itemId, now)

        val updated = SpacedRepetitionEngine.nextState(current, wasCorrect, now)
        vocabularyDao.upsertReview(updated)

        val request = VocabularyReviewRequest(itemId = itemId, correct = wasCorrect)
        val trySendNow = runCatching { apiService.submitVocabularyReview(itemId, request) }

        if (trySendNow.isFailure) {
            syncQueueDao.enqueue(
                SyncQueueEntity(
                    actionType = "vocabulary_review",
                    payloadJson = gson.toJson(request),
                    createdAtEpochMs = now,
                )
            )
        }
    }
}
