package com.example.myndchallenge.data

import com.example.myndchallenge.data.api.ApiException
import com.example.myndchallenge.data.api.MyndApiClient
import com.example.myndchallenge.data.repository.CheckInRepository
import com.example.myndchallenge.data.repository.ConfigRepository
import com.example.myndchallenge.domain.Mood
import com.example.myndchallenge.json
import com.example.myndchallenge.routes
import com.example.myndchallenge.testApi
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CheckInRepositoryTest {
    @Test
    fun streakIsReadFromTheApi() =
        runTest {
            val api = routes("check-ins/streak" to """{"data":{"current_streak_days":3,"last_check_in_date":"2026-10-11"}}""")

            val streak = CheckInRepository(api).streak().getOrThrow()

            assertEquals(3, streak.days)
            assertEquals("2026-10-11", streak.lastCheckInDate)
        }

    @Test
    fun serverErrorsBecomeFailures() =
        runTest {
            val api = testApi { json("""{"message":"Server Error"}""", HttpStatusCode.InternalServerError) }

            val result = CheckInRepository(api).streak()

            assertTrue(result.isFailure)
            assertIs<ApiException>(result.exceptionOrNull()).also { assertEquals(500, it.status) }
        }

    @Test
    fun createSendsTokenAndTrimmedBody() =
        runTest {
            var captured: HttpRequestData? = null
            val api =
                testApi { request ->
                    captured = request
                    json("""{"message":"stop here"}""", HttpStatusCode.UnprocessableEntity)
                }

            CheckInRepository(api).create(Mood.TIRED, 4, "  long day  ")

            val request = assertNotNull(captured)
            assertEquals(HttpMethod.Post, request.method)
            assertEquals("/api/check-ins", request.url.encodedPath)
            assertEquals("test-token", request.headers[MyndApiClient.TOKEN_HEADER])
            val body = (request.body as TextContent).text
            assertEquals("""{"mood":"tired","intensity":4,"note":"long day"}""", body)
        }

    @Test
    fun createRejectsOutOfRangeIntensityWithoutCallingTheApi() =
        runTest {
            var calls = 0
            val api =
                testApi {
                    calls++
                    json("{}")
                }

            val result = CheckInRepository(api).create(Mood.HAPPY, 9, null)

            assertTrue(result.isFailure)
            assertEquals(0, calls)
        }

    @Test
    fun configFailureMeansEveryFeatureIsOff() =
        runTest {
            val api = testApi { json("""{"message":"Unauthenticated."}""", HttpStatusCode.Unauthorized) }

            assertFalse(ConfigRepository(api).features().insights)
        }
}
