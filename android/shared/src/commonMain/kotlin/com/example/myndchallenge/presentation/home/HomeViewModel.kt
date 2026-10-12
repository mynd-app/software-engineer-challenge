package com.example.myndchallenge.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myndchallenge.data.repository.CheckInRepository
import com.example.myndchallenge.data.repository.ConfigRepository
import com.example.myndchallenge.domain.CheckIn
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val streakDays: Int = 0,
    val recent: List<CheckIn> = emptyList(),
    val insightsEnabled: Boolean = false,
    val error: String? = null,
)

class HomeViewModel(
    private val checkIns: CheckInRepository,
    private val config: ConfigRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val streak = async { checkIns.streak() }
            val recent = async { checkIns.recent() }
            val features = async { config.features() }

            val recentResult = recent.await()
            _state.value =
                HomeUiState(
                    isLoading = false,
                    streakDays = streak.await().getOrNull()?.days ?: 0,
                    recent = recentResult.getOrDefault(emptyList()),
                    insightsEnabled = features.await().insights,
                    error = recentResult.exceptionOrNull()?.let { "Couldn't load your recent check-ins." },
                )
        }
    }
}
