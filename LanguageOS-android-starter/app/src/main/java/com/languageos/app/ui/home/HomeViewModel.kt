package com.languageos.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.languageos.app.data.local.CourseEntity
import com.languageos.app.data.repository.LessonRepository
import com.languageos.app.data.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val courses: List<CourseEntity> = emptyList(),
    val dueVocabularyCount: Int = 0,
    val isLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val vocabularyRepository: VocabularyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            lessonRepository.observeCourses().collect { courses ->
                _uiState.value = _uiState.value.copy(courses = courses, isLoading = false)
            }
        }
        viewModelScope.launch {
            val due = vocabularyRepository.getDueItems()
            _uiState.value = _uiState.value.copy(dueVocabularyCount = due.size)
        }
    }
}
