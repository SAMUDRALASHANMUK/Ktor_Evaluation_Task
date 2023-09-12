package com.example.routes

import com.example.model.request.UserRegistration
import com.example.util.appconstants.ApiEndPoints
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for the User API routes.
 */
class UserRoutesTest {

    /**
     * Test the GET /users endpoint.
     * This test ensures that the list of users can be retrieved successfully.
     */
    @Test
    fun `get users should return 200 OK`(): Unit = testApplication {

        runBlocking {
            val response = client.get(ApiEndPoints.USER)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    /**
     * Test the POST /users endpoint to create a new user.
     * This test creates a new user with the provided registration details and
     * checks if the endpoint returns a 201 Created status.
     */

    @Test
    fun `create user should return 201 Created`(): Unit = testApplication {
        val userRegistration = UserRegistration("userName", "password")
        val serializedUserRegistration = Json.encodeToString(userRegistration)
        runBlocking {
            val response = client.post(ApiEndPoints.USER) {
                headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                setBody(serializedUserRegistration)
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }
}
