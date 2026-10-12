package com.example.myndchallenge.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfigDto(
    val features: FeaturesDto = FeaturesDto(),
)

/**
 * Server-controlled feature flags. Every flag defaults to false: a missing key, an old backend
 * or a failed request must never switch a feature on.
 */
@Serializable
data class FeaturesDto(
    val insights: Boolean = false,
)
