package com.example.myndchallenge.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreakDto(
    @SerialName("current_streak_days") val currentStreakDays: Int,
    /** `YYYY-MM-DD` in the user's own timezone, or null if they have never checked in. */
    @SerialName("last_check_in_date") val lastCheckInDate: String? = null,
)
