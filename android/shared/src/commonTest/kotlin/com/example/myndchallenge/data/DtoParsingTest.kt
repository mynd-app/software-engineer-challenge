package com.example.myndchallenge.data

import com.example.myndchallenge.data.api.MyndApiClient
import com.example.myndchallenge.data.dto.ConfigDto
import com.example.myndchallenge.data.dto.Envelope
import com.example.myndchallenge.data.dto.StreakDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DtoParsingTest {
    private val json = MyndApiClient.json

    @Test
    fun streakParsesSnakeCaseFields() {
        val streak =
            json.decodeFromString<Envelope<StreakDto>>(
                """{"data":{"current_streak_days":4,"last_check_in_date":"2026-10-11"}}""",
            ).data
        assertEquals(4, streak.currentStreakDays)
        assertEquals("2026-10-11", streak.lastCheckInDate)
    }

    @Test
    fun streakWithNoCheckInsHasNullDate() {
        val streak =
            json.decodeFromString<Envelope<StreakDto>>(
                """{"data":{"current_streak_days":0,"last_check_in_date":null}}""",
            ).data
        assertEquals(0, streak.currentStreakDays)
        assertNull(streak.lastCheckInDate)
    }

    @Test
    fun configReadsInsightsFlag() {
        val config = json.decodeFromString<Envelope<ConfigDto>>("""{"data":{"features":{"insights":true}}}""").data
        assertTrue(config.features.insights)
    }

    @Test
    fun missingFlagsFailClosed() {
        assertFalse(json.decodeFromString<Envelope<ConfigDto>>("""{"data":{"features":{}}}""").data.features.insights)
        assertFalse(json.decodeFromString<Envelope<ConfigDto>>("""{"data":{}}""").data.features.insights)
    }

    @Test
    fun unknownKeysAreIgnored() {
        val config =
            json.decodeFromString<Envelope<ConfigDto>>(
                """{"data":{"features":{"insights":false,"something_new":true}},"meta":{}}""",
            ).data
        assertFalse(config.features.insights)
    }
}
