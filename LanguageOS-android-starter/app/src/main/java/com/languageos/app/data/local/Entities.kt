package com.languageos.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val nativeLang: String,
    val targetLang: String,
    val currentLevel: String, // تخمین کلی CEFR، مثلاً "B1"
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val targetLang: String,
    val cefrLevel: String,
)

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val title: String,
    val type: String, // vocabulary | grammar | listening | speaking
    val orderIndex: Int,
    val durationMinutes: Int,
    val isDownloaded: Boolean = false,
)

@Entity(tableName = "vocabulary_items")
data class VocabularyItemEntity(
    @PrimaryKey val id: String,
    val lessonId: String,
    val term: String,
    val translation: String,
    val audioUrl: String?,
)

enum class SrsState { NEW, LEARNING, FAMILIAR, KNOWN, MASTERED }

@Entity(tableName = "vocabulary_reviews")
data class VocabularyReviewEntity(
    @PrimaryKey val itemId: String,
    val state: SrsState = SrsState.NEW,
    val intervalDays: Int = 1,
    val nextReviewAtEpochMs: Long,
    val lastResultCorrect: Boolean? = null,
)

@Entity(tableName = "progress_snapshots")
data class ProgressSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val skill: String, // vocabulary | grammar | listening | speaking | pronunciation
    val score: Int, // 0-100
    val capturedAtEpochMs: Long,
)

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actionType: String, // مثلاً "vocabulary_review"
    val payloadJson: String,
    val createdAtEpochMs: Long,
    val attemptCount: Int = 0,
)

class Converters {
    @TypeConverter
    fun fromSrsState(value: SrsState): String = value.name

    @TypeConverter
    fun toSrsState(value: String): SrsState = SrsState.valueOf(value)
}
