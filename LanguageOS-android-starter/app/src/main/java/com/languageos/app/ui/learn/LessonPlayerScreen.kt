package com.languageos.app.ui.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
fun LessonPlayerScreen(
    lessonId: String,
    onFinished: () -> Unit,
    viewModel: LessonPlayerViewModel = hiltViewModel(),
) {
    LaunchedEffect(lessonId) { viewModel.load(lessonId) }
    val state by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val currentItem = state.currentItem
            if (currentItem == null) {
                Text("درس تمام شد 🎉")
                Button(onClick = onFinished) { Text("بازگشت") }
            } else {
                Text(text = currentItem.term, style = MaterialTheme.typography.headlineMedium)
                Text(text = currentItem.translation)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(onClick = { viewModel.answer(correct = false) }) { Text("بلد نبودم") }
                    Button(onClick = { viewModel.answer(correct = true) }) { Text("بلد بودم") }
                }
            }
        }
    }
}
