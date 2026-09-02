package com.languageos.app.data.repository

import com.languageos.app.data.local.CourseDao
import com.languageos.app.data.local.CourseEntity
import com.languageos.app.data.local.LessonDao
import com.languageos.app.data.local.LessonEntity
import com.languageos.app.data.local.VocabularyDao
import com.languageos.app.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepository @Inject constructor(
    private val courseDao: CourseDao,
    private val lessonDao: LessonDao,
    private val vocabularyDao: VocabularyDao,
    private val apiService: ApiService,
) {

    fun observeCourses(): Flow<List<CourseEntity>> = courseDao.observeCourses()

    fun observeLessons(courseId: String): Flow<List<LessonEntity>> =
        lessonDao.observeLessonsForCourse(courseId)

    /**
     * UI همیشه از Room تغذیه می‌شود (observeLessons بالا)، پس اگر این تابع به‌خاطر
     * نبود اینترنت شکست بخورد، مشکلی نیست — این دقیقاً رفتار Offline-first بخش ۷ PRD است.
     */
    suspend fun refreshFromServer(courseId: String) {
        runCatching {
            val lessons = apiService.getLessons(courseId)
            lessonDao.upsertAll(
                lessons.map {
                    LessonEntity(
                        id = it.id,
                        courseId = it.courseId,
                        title = it.title,
                        type = it.type,
                        orderIndex = it.orderIndex,
                        durationMinutes = it.durationMinutes,
                    )
                }
            )
        }
    }

    suspend fun getLesson(lessonId: String): LessonEntity? = lessonDao.getLesson(lessonId)
}
