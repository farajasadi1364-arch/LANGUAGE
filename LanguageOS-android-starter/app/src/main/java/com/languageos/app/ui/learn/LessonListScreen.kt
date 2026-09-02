package com.languageos.app.ui.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LessonListScreen(
    courseId: String,
    onLessonSelected: (lessonId: String) -> Unit,
    viewModel: LessonViewModel = hiltViewModel(),
) {
    LaunchedEffect(courseId) { viewModel.load(courseId) }
    val lessons by viewModel.lessons.collectAsState()

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(lessons) { lesson ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onLessonSelected(lesson.id) },
                ) {
                    Text(
                        text = "${lesson.title} · ${lesson.durationMinutes} دقیقه",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}
