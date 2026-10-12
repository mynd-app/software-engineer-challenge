package com.example.myndchallenge.domain

data class CheckIn(
    val id: Long,
    /** Null when the backend sends a mood this app version doesn't know yet. */
    val mood: Mood?,
    val intensity: Int,
    val note: String?,
    /** ISO-8601 UTC timestamp from the backend. */
    val createdAt: String,
)

data class Streak(
    val days: Int,
    val lastCheckInDate: String?,
)
