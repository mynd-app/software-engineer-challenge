package com.example.myndchallenge.data.api

import io.ktor.client.engine.HttpClientEngine

/** The real HTTP engine for this platform. Tests pass a MockEngine instead. */
expect fun platformHttpEngine(): HttpClientEngine
