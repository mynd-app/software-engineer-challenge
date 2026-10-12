package com.example.myndchallenge.data.repository

import com.example.myndchallenge.data.api.MyndApiClient
import com.example.myndchallenge.data.dto.FeaturesDto

class ConfigRepository(
    private val api: MyndApiClient,
) {
    /** Never fails: if the config can't be fetched, every feature is off. */
    suspend fun features(): FeaturesDto = resultOf { api.getConfig().features }.getOrDefault(FeaturesDto())
}
