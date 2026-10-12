package com.example.myndchallenge.data.dto

import kotlinx.serialization.Serializable

/** Every API response is wrapped as `{"data": ...}`. */
@Serializable
data class Envelope<T>(
    val data: T,
)
