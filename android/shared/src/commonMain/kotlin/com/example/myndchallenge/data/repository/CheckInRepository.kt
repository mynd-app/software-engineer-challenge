package com.example.myndchallenge.data.repository

import com.example.myndchallenge.data.api.MyndApiClient
import com.example.myndchallenge.data.dto.CheckInDto
import com.example.myndchallenge.data.dto.CreateCheckInRequest
import com.example.myndchallenge.domain.CheckIn
import com.example.myndchallenge.domain.Mood
import com.example.myndchallenge.domain.Streak

class CheckInRepository(
    private val api: MyndApiClient,
) {
    suspend fun recent(limit: Int = 20): Result<List<CheckIn>> =
        resultOf { api.getCheckIns(limit).map { it.toDomain() } }

    suspend fun streak(): Result<Streak> =
        resultOf {
            val dto = api.getStreak()
            Streak(days = dto.currentStreakDays, lastCheckInDate = dto.lastCheckInDate)
        }

    suspend fun create(
        mood: Mood,
        intensity: Int,
        note: String?,
    ): Result<CheckIn> =
        resultOf {
            require(intensity in 1..5) { "intensity must be 1-5" }
            api
                .createCheckIn(
                    CreateCheckInRequest(
                        mood = mood.apiValue,
                        intensity = intensity,
                        note = note?.trim()?.takeIf { it.isNotEmpty() },
                    ),
                ).toDomain()
        }
}

private fun CheckInDto.toDomain() =
    CheckIn(
        id = id,
        mood = Mood.fromApi(mood),
        intensity = intensity,
        note = note,
        createdAt = createdAt,
    )
