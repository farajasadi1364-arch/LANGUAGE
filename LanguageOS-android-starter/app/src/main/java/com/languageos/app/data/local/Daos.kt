package com.languageos.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun observeCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(courses: List<CourseEntity>)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY orderIndex ASC")
    fun observeLessonsForCourse(courseId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    suspend fun getLesson(lessonId: String): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(lessons: List<LessonEntity>)
}

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_items WHERE lessonId = :lessonId")
    suspend fun getItemsForLesson(lessonId: String): List<VocabularyItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItems(items: List<VocabularyItemEntity>)

    @Query(
        """
        SELECT vocabulary_items.* FROM vocabulary_items
        INNER JOIN vocabulary_reviews ON vocabulary_items.id = vocabulary_reviews.itemId
        WHERE vocabulary_reviews.nextReviewAtEpochMs <= :nowEpochMs
        ORDER BY vocabulary_reviews.nextReviewAtEpochMs ASC
        LIMIT :limit
        """
    )
    suspend fun getDueForReview(nowEpochMs: Long, limit: Int = 20): List<VocabularyItemEntity>

    @Query("SELECT * FROM vocabulary_reviews WHERE itemId = :itemId")
    suspend fun getReviewState(itemId: String): VocabularyReviewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReview(review: VocabularyReviewEntity)
}

@Dao
interface ProgressDao {
    @Insert
    suspend fun insertSnapshot(snapshot: ProgressSnapshotEntity)

    @Query("SELECT * FROM progress_snapshots WHERE skill = :skill ORDER BY capturedAtEpochMs DESC LIMIT 1")
    suspend fun getLatestForSkill(skill: String): ProgressSnapshotEntity?
}

@Dao
interface SyncQueueDao {
    @Insert
    suspend fun enqueue(item: SyncQueueEntity)

    @Query("SELECT * FROM sync_queue ORDER BY createdAtEpochMs ASC")
    suspend fun getPending(): List<SyncQueueEntity>

    @Update
    suspend fun update(item: SyncQueueEntity)

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun remove(id: Long)
}
