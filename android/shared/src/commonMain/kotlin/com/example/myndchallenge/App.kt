package com.example.myndchallenge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myndchallenge.presentation.checkin.CheckInScreen
import com.example.myndchallenge.presentation.checkin.CheckInViewModel
import com.example.myndchallenge.presentation.home.HomeScreen
import com.example.myndchallenge.presentation.home.HomeViewModel
import com.example.myndchallenge.ui.theme.MyndTheme
import org.koin.compose.viewmodel.koinViewModel

/** The screens the app can show. Navigation is a simple back-to-Home model. */
sealed interface Screen {
    data object Home : Screen

    data object CheckIn : Screen
}

@Composable
fun App() {
    MyndTheme {
        var screen by remember { mutableStateOf<Screen>(Screen.Home) }
        val home: HomeViewModel = koinViewModel()

        when (screen) {
            Screen.Home -> {
                val state by home.state.collectAsStateWithLifecycle()
                HomeScreen(
                    state = state,
                    onCheckIn = { screen = Screen.CheckIn },
                    onRetry = home::refresh,
                )
            }

            Screen.CheckIn -> {
                val vm: CheckInViewModel = koinViewModel()
                val state by vm.state.collectAsStateWithLifecycle()
                LaunchedEffect(state.submitted) {
                    if (state.submitted) {
                        vm.reset()
                        home.refresh()
                        screen = Screen.Home
                    }
                }
                CheckInScreen(
                    state = state,
                    onMood = vm::selectMood,
                    onIntensity = vm::setIntensity,
                    onNote = vm::setNote,
                    onSubmit = vm::submit,
                    onBack = { screen = Screen.Home },
                )
            }
        }
    }
}
