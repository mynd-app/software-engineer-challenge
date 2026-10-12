package com.example.myndchallenge.presentation.checkin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myndchallenge.domain.Mood
import com.example.myndchallenge.ui.components.MyndScaffold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInScreen(
    state: CheckInUiState,
    onMood: (Mood) -> Unit,
    onIntensity: (Int) -> Unit,
    onNote: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    MyndScaffold(title = "Check in", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("How are you feeling?", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Mood.entries.forEach { mood ->
                    FilterChip(
                        selected = state.mood == mood,
                        onClick = { onMood(mood) },
                        label = { Text(mood.label) },
                    )
                }
            }

            Text("How strongly? ${state.intensity}/5", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { value ->
                    FilterChip(
                        selected = state.intensity == value,
                        onClick = { onIntensity(value) },
                        label = { Text("$value") },
                    )
                }
            }

            OutlinedTextField(
                value = state.note,
                onValueChange = onNote,
                label = { Text("Anything you want to add? (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(onClick = onSubmit, enabled = state.canSubmit, modifier = Modifier.fillMaxWidth()) {
                Text(if (state.isSubmitting) "Saving..." else "Save check-in")
            }
        }
    }
}
