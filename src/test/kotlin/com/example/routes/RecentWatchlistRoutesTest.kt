package com.example.routes

import com.example.util.appconstants.ApiEndPoints
import com.example.utils.appconstants.GlobalConstants.GET_RECENT_WATCHLIST_TEST
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for the Recent Watchlist API routes.
 */
class RecentWatchlistRoutesTest {
    /**
     * Test the GET /recent-watchlist/{userId} endpoint.
     * This test ensures that the recent watchlist can be retrieved successfully.
     */
    @Test
    fun `get recent watchlist should return 200 OK`(): Unit = testApplication {

        runBlocking {
            val response = client.get(ApiEndPoints.RECENT_WATCHLIST + GET_RECENT_WATCHLIST_TEST)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    /**
     * Test the GET /recent-watchlist/{userId} endpoint with an invalid UUID.
     * This test ensures that the endpoint returns a 400 Bad Request status when an invalid UUID is provided.
     */
    @Test
    fun `get recent watchlist with invalid UUID should return 400 Bad Request`() = testApplication {
        runBlocking {
            val response = client.get(ApiEndPoints.RECENT_WATCHLIST + "/invalidUUID")
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

}
