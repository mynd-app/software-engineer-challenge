package com.example.myndchallenge.presentation.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myndchallenge.data.repository.CheckInRepository
import com.example.myndchallenge.domain.Mood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckInUiState(
    val mood: Mood? = null,
    val intensity: Int = 3,
    val note: String = "",
    val isSubmitting: Boolean = false,
    val submitted: Boolean = false,
    val error: String? = null,
) {
    val canSubmit: Boolean get() = mood != null && !isSubmitting
}

class CheckInViewModel(
    private val checkIns: CheckInRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CheckInUiState())
    val state: StateFlow<CheckInUiState> = _state.asStateFlow()

    fun selectMood(mood: Mood) = _state.update { it.copy(mood = mood, error = null) }

    fun setIntensity(value: Int) = _state.update { it.copy(intensity = value.coerceIn(1, 5)) }

    fun setNote(value: String) = _state.update { it.copy(note = value.take(MAX_NOTE_LENGTH)) }

    fun submit() {
        val current = _state.value
        val mood = current.mood ?: return
        if (current.isSubmitting) return
        _state.update { it.copy(isSubmitting = true, error = null) }
        viewModelScope.launch {
            checkIns
                .create(mood, current.intensity, current.note)
                .onSuccess { _state.update { it.copy(isSubmitting = false, submitted = true) } }
                .onFailure { _state.update { it.copy(isSubmitting = false, error = "Couldn't save your check-in. Try again.") } }
        }
    }

    /** Call after the screen has reacted to [CheckInUiState.submitted]. */
    fun reset() {
        _state.value = CheckInUiState()
    }

    companion object {
        const val MAX_NOTE_LENGTH = 500
    }
}
