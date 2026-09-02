package com.languageos.app.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class CourseDto(
    val id: String,
    val title: String,
    val targetLang: String,
    val cefrLevel: String,
)

data class LessonDto(
    val id: String,
    val courseId: String,
    val title: String,
    val type: String,
    val orderIndex: Int,
    val durationMinutes: Int,
)

data class VocabularyItemDto(
    val id: String,
    val lessonId: String,
    val term: String,
    val translation: String,
    val audioUrl: String?,
)

data class VocabularyReviewRequest(
    val itemId: String,
    val correct: Boolean,
)

data class LessonAttemptRequest(
    val lessonId: String,
    val score: Int,
)

interface ApiService {

    @GET("courses")
    suspend fun getCourses(): List<CourseDto>

    @GET("courses/{courseId}/lessons")
    suspend fun getLessons(@Path("courseId") courseId: String): List<LessonDto>

    @GET("lessons/{lessonId}/vocabulary")
    suspend fun getVocabularyForLesson(@Path("lessonId") lessonId: String): List<VocabularyItemDto>

    @GET("vocabulary/due")
    suspend fun getDueVocabulary(@Query("limit") limit: Int = 20): List<VocabularyItemDto>

    @POST("vocabulary/{itemId}/review")
    suspend fun submitVocabularyReview(
        @Path("itemId") itemId: String,
        @Body body: VocabularyReviewRequest,
    )

    @POST("lessons/{lessonId}/attempt")
    suspend fun submitLessonAttempt(
        @Path("lessonId") lessonId: String,
        @Body body: LessonAttemptRequest,
    )
}
