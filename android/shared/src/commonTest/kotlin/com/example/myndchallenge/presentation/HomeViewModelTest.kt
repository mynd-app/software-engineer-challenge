package com.example.myndchallenge.presentation

import com.example.myndchallenge.data.repository.CheckInRepository
import com.example.myndchallenge.data.repository.ConfigRepository
import com.example.myndchallenge.presentation.checkin.CheckInViewModel
import com.example.myndchallenge.presentation.home.HomeUiState
import com.example.myndchallenge.presentation.home.HomeViewModel
import com.example.myndchallenge.domain.Mood
import com.example.myndchallenge.routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun loadsStreakAndFeatureFlags() =
        runTest {
            val api =
                routes(
                    "check-ins/streak" to """{"data":{"current_streak_days":5,"last_check_in_date":"2026-10-11"}}""",
                    "check-ins" to """{"data":[]}""",
                    "config" to """{"data":{"features":{"insights":true}}}""",
                )

            val vm = HomeViewModel(CheckInRepository(api), ConfigRepository(api))

            val state = vm.loaded()
            assertEquals(5, state.streakDays)
            assertTrue(state.recent.isEmpty())
            assertTrue(state.insightsEnabled)
            assertNull(state.error)
        }

    @Test
    fun insightsStaysOffWhenConfigIsMissing() =
        runTest {
            val api =
                routes(
                    "check-ins/streak" to """{"data":{"current_streak_days":0,"last_check_in_date":null}}""",
                    "check-ins" to """{"data":[]}""",
                )

            val vm = HomeViewModel(CheckInRepository(api), ConfigRepository(api))

            assertFalse(vm.loaded().insightsEnabled)
        }

    @Test
    fun checkInCannotBeSubmittedWithoutAMood() =
        runTest {
            val vm = CheckInViewModel(CheckInRepository(routes()))

            assertFalse(vm.state.value.canSubmit)
            vm.selectMood(Mood.CALM)
            assertTrue(vm.state.value.canSubmit)
        }

    @Test
    fun intensityIsClampedAndNoteIsCapped() =
        runTest {
            val vm = CheckInViewModel(CheckInRepository(routes()))

            vm.setIntensity(0)
            assertEquals(1, vm.state.value.intensity)
            vm.setIntensity(7)
            assertEquals(5, vm.state.value.intensity)
            vm.setNote("x".repeat(600))
            assertEquals(CheckInViewModel.MAX_NOTE_LENGTH, vm.state.value.note.length)
        }

    // The API calls finish on Ktor's own dispatcher, so wait for the load to complete rather than
    // reading whatever state happens to be current.
    private suspend fun HomeViewModel.loaded(): HomeUiState = state.first { !it.isLoading }
}
