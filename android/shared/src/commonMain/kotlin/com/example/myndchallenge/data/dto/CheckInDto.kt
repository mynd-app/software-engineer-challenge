package com.example.myndchallenge.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckInDto(
    val id: Long,
    val mood: String,
    val intensity: Int,
    val note: String? = null,
    val createdAt: String,
)

@Serializable
data class CreateCheckInRequest(
    val mood: String,
    val intensity: Int,
    val note: String? = null,
)
