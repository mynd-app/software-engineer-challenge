package com.example.myndchallenge.data.api

/** A non-2xx response from the backend. */
class ApiException(
    val status: Int,
    message: String,
) : Exception(message)
