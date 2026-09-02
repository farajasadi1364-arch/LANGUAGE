package com.languageos.app.domain

import com.languageos.app.data.local.SrsState
import com.languageos.app.data.local.VocabularyReviewEntity
import java.util.concurrent.TimeUnit

/**
 * پیاده‌سازی منطق Spaced Repetition طبق بخش ۶ سند PRD.
 * فاصله‌ها: 1d → 3d → 7d → 14d → 30d
 * پاسخ غلط → بازگشت به حداقل فاصله (1 روز)، نه صفر کامل (برای جلوگیری از دلسردی کاربر).
 */
object SpacedRepetitionEngine {

    private val intervalSteps = listOf(1, 3, 7, 14, 30) // روز

    fun nextState(current: VocabularyReviewEntity, wasCorrect: Boolean, nowEpochMs: Long): VocabularyReviewEntity {
        val currentStepIndex = intervalSteps.indexOf(current.intervalDays).let { if (it == -1) 0 else it }

        val newStepIndex = if (wasCorrect) {
            (currentStepIndex + 1).coerceAtMost(intervalSteps.lastIndex)
        } else {
            0 // بازگشت به حداقل فاصله
        }

        val newInterval = intervalSteps[newStepIndex]
        val newState = when {
            !wasCorrect -> SrsState.LEARNING
            newStepIndex >= 4 -> SrsState.MASTERED
            newStepIndex >= 2 -> SrsState.KNOWN
            newStepIndex >= 1 -> SrsState.FAMILIAR
            else -> SrsState.LEARNING
        }

        return current.copy(
            state = newState,
            intervalDays = newInterval,
            nextReviewAtEpochMs = nowEpochMs + TimeUnit.DAYS.toMillis(newInterval.toLong()),
            lastResultCorrect = wasCorrect,
        )
    }

    fun initialState(itemId: String, nowEpochMs: Long): VocabularyReviewEntity = VocabularyReviewEntity(
        itemId = itemId,
        state = SrsState.NEW,
        intervalDays = 1,
        nextReviewAtEpochMs = nowEpochMs,
        lastResultCorrect = null,
    )
}
