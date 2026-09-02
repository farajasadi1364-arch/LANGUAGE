package com.languageos.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onStartLearning: (courseId: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = "🎯 هدف امروز", style = MaterialTheme.typography.titleLarge)

            if (state.dueVocabularyCount > 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "🧠 مرور هوشمند: ${state.dueVocabularyCount} کلمه",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            Text(text = "دوره‌های من", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.courses) { course ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = course.title, style = MaterialTheme.typography.titleMedium)
                            Text(text = "سطح: ${course.cefrLevel}")
                            Button(onClick = { onStartLearning(course.id) }) {
                                Text("شروع یادگیری")
                            }
                        }
                    }
                }
            }
        }
    }
}
