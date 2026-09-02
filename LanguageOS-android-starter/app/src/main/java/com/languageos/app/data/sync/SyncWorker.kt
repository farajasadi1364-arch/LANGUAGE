package com.languageos.app.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.languageos.app.data.local.SyncQueueDao
import com.languageos.app.data.remote.ApiService
import com.languageos.app.data.remote.VocabularyReviewRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * پیاده‌سازی بخش ۱۴ سند PRD: وقتی اینترنت برگردد، این Worker صف Sync محلی
 * را یکی‌یکی به سرور می‌فرستد. زمان‌بندی Retry/Backoff از طریق WorkManager
 * (setBackoffCriteria) موقع enqueue کردن این Worker تنظیم می‌شود.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncQueueDao: SyncQueueDao,
    private val apiService: ApiService,
    private val gson: Gson,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val pending = syncQueueDao.getPending()
        if (pending.isEmpty()) return Result.success()

        var anyFailure = false

        for (item in pending) {
            val sent = runCatching {
                when (item.actionType) {
                    "vocabulary_review" -> {
                        val body = gson.fromJson(item.payloadJson, VocabularyReviewRequest::class.java)
                        apiService.submitVocabularyReview(body.itemId, body)
                    }
                    // TODO: سایر actionType ها (lesson_attempt، homework_submission و...) طبق نیاز اضافه شود
                }
            }

            if (sent.isSuccess) {
                syncQueueDao.remove(item.id)
            } else {
                anyFailure = true
            }
        }

        return if (anyFailure) Result.retry() else Result.success()
    }
}
