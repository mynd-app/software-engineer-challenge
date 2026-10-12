package com.example.myndchallenge.data.api

import com.example.myndchallenge.data.dto.CheckInDto
import com.example.myndchallenge.data.dto.ConfigDto
import com.example.myndchallenge.data.dto.CreateCheckInRequest
import com.example.myndchallenge.data.dto.Envelope
import com.example.myndchallenge.data.dto.StreakDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Thin wrapper over the backend API. One method per endpoint; no business logic here.
 * Every response is wrapped in `{"data": ...}`, which this class unwraps.
 */
class MyndApiClient(
    config: ApiConfig,
    engine: HttpClientEngine,
) {
    private val client =
        HttpClient(engine) {
            install(ContentNegotiation) { json(json) }
            defaultRequest {
                url(config.baseUrl)
                header(TOKEN_HEADER, config.token)
                contentType(ContentType.Application.Json)
            }
        }

    suspend fun getConfig(): ConfigDto = client.get("config").unwrap()

    suspend fun getStreak(): StreakDto = client.get("check-ins/streak").unwrap()

    suspend fun getCheckIns(limit: Int): List<CheckInDto> =
        client.get("check-ins") { parameter("limit", limit) }.unwrap()

    suspend fun createCheckIn(request: CreateCheckInRequest): CheckInDto =
        client.post("check-ins") { setBody(request) }.unwrap()

    private suspend inline fun <reified T> HttpResponse.unwrap(): T {
        if (!status.isSuccess()) {
            throw ApiException(status.value, "HTTP ${status.value}: ${bodyAsText().take(200)}")
        }
        return body<Envelope<T>>().data
    }

    companion object {
        const val TOKEN_HEADER = "X-Mynd-Token"

        val json =
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
    }
}
