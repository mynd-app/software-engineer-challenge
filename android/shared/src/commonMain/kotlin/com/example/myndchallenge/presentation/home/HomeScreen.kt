package com.example.myndchallenge.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myndchallenge.domain.CheckIn
import com.example.myndchallenge.ui.components.ErrorMessage
import com.example.myndchallenge.ui.components.MyndScaffold

@Composable
fun HomeScreen(
    state: HomeUiState,
    onCheckIn: () -> Unit,
    onRetry: () -> Unit,
) {
    MyndScaffold(title = "mynd") { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { StreakCard(state) }
            item {
                Button(onClick = onCheckIn, modifier = Modifier.fillMaxWidth()) {
                    Text("How are you feeling?")
                }
            }
            item {
                Text(
                    "Recent check-ins",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            when {
                state.isLoading && state.recent.isEmpty() -> item { CircularProgressIndicator() }
                state.error != null -> item { ErrorMessage(state.error, onRetry) }
                state.recent.isEmpty() -> item { Text("No check-ins yet.") }
                else -> items(state.recent, key = { it.id }) { CheckInRow(it) }
            }
        }
    }
}

@Composable
private fun StreakCard(state: HomeUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Your streak", style = MaterialTheme.typography.labelLarge)
            Text(
                if (state.isLoading) "..." else "${state.streakDays} day streak",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun CheckInRow(checkIn: CheckIn) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(Modifier.weight(1f)) {
            Text(checkIn.mood?.label ?: "Other", style = MaterialTheme.typography.bodyLarge)
            checkIn.note?.let { Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 2) }
        }
        Column {
            Text("${checkIn.intensity}/5", style = MaterialTheme.typography.bodyMedium)
            Text(checkIn.createdAt.take(10), style = MaterialTheme.typography.bodySmall)
        }
    }
}
