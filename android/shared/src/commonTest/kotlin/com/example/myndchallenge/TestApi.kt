package com.example.myndchallenge

import com.example.myndchallenge.data.api.ApiConfig
import com.example.myndchallenge.data.api.MyndApiClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

val testConfig = ApiConfig(baseUrl = "http://test.local/api/", token = "test-token")

/** Builds an API client whose requests are answered by [handler] instead of the network. */
fun testApi(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): MyndApiClient =
    MyndApiClient(testConfig, MockEngine(handler))

fun MockRequestHandleScope.json(
    body: String,
    status: HttpStatusCode = HttpStatusCode.OK,
): HttpResponseData = respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))

/** Routes by path (relative to /api/); unknown paths get a 404. */
fun routes(vararg pairs: Pair<String, String>): MyndApiClient {
    val table = pairs.toMap()
    return testApi { request ->
        val path = request.url.encodedPath.removePrefix("/api/")
        table[path]?.let { json(it) } ?: json("""{"message":"Not found"}""", HttpStatusCode.NotFound)
    }
}
