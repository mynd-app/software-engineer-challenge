package com.example.myndchallenge.data.api

/**
 * Where the app talks to and who it talks as.
 *
 * [baseUrl] must end with a slash, e.g. `http://10.0.2.2:8080/api/`.
 * [token] is sent on every request as the `X-Mynd-Token` header.
 */
data class ApiConfig(
    val baseUrl: String,
    val token: String,
)
