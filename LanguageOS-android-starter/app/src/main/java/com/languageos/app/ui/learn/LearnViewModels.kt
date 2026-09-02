package com.languageos.app.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.languageos.app.data.local.LessonEntity
import com.languageos.app.data.local.VocabularyItemEntity
import com.languageos.app.data.repository.LessonRepository
import com.languageos.app.data.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
) : ViewModel() {

    private val _lessons = MutableStateFlow<List<LessonEntity>>(emptyList())
    val lessons: StateFlow<List<LessonEntity>> = _lessons.asStateFlow()

    fun load(courseId: String) {
        viewModelScope.launch {
            lessonRepository.observeLessons(courseId).collect { _lessons.value = it }
        }
        viewModelScope.launch {
            lessonRepository.refreshFromServer(courseId) // best-effort؛ آفلاین بی‌صدا شکست می‌خورد
        }
    }
}

data class LessonPlayerUiState(
    val queue: List<VocabularyItemEntity> = emptyList(),
    val currentIndex: Int = 0,
) {
    val currentItem: VocabularyItemEntity? get() = queue.getOrNull(currentIndex)
}

@HiltViewModel
class LessonPlayerViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonPlayerUiState())
    val uiState: StateFlow<LessonPlayerUiState> = _uiState.asStateFlow()

    fun load(lessonId: String) {
        viewModelScope.launch {
            // v1 ساده: آیتم‌های سررسیدشده کل اپ را نشان می‌دهد.
            // نسخه کامل باید این‌ها را طبق lessonId هم فیلتر کند.
            val items = vocabularyRepository.getDueItems()
            _uiState.value = LessonPlayerUiState(queue = items)
        }
    }

    fun answer(correct: Boolean) {
        val item = _uiState.value.currentItem ?: return
        viewModelScope.launch {
            vocabularyRepository.submitReview(item.id, correct)
            _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex + 1)
        }
    }
}
