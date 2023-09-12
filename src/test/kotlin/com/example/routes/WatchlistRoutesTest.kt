package com.example.routes

import com.example.database.table.RecentWatchList
import com.example.database.table.Users
import com.example.database.table.Watchlists
import com.example.model.request.DeleteWatchlist
import com.example.model.request.UpdateWatchlist
import com.example.model.request.WatchlistCreation
import com.example.util.appconstants.ApiEndPoints
import com.example.utils.TestDatabase
import com.example.utils.appconstants.GlobalConstants.GET_ALL_WATCHLIST_FOR_USER_TEST
import com.example.utils.appconstants.GlobalConstants.USER_ID_TEST
import com.example.utils.appconstants.GlobalConstants.WATCHLIST_ID_TEST
import com.example.utils.appconstants.GlobalConstants.symbolsToAdd
import com.example.utils.appconstants.GlobalConstants.updatedSymbols
import com.example.utils.testhelpers.createUserForTesting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.util.*
import kotlin.test.assertEquals

/**
 * Unit tests for the Watchlist API routes.
 */
class WatchlistRoutesTest {
    private lateinit var database: Database
    private lateinit var testUserId: UUID

    @Before
    fun setup() {
        database = TestDatabase.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Users, Watchlists, RecentWatchList)
        }
        testUserId = createUserForTesting()
    }

    @After
    fun tearDown() {
        stopKoin()
        transaction(database) {
            SchemaUtils.drop(Users, Watchlists, RecentWatchList)
        }
    }

    /**
     * Test the create watchlist endpoint.
     */
    @Test
    fun `create watchlist should return 201 Created`() = testApplication {
        runBlocking {
            val watchlistCreation = WatchlistCreation(USER_ID_TEST, symbolsToAdd)
            val serializedWatchlistCreation = Json.encodeToString(watchlistCreation)
            val response = client.post(ApiEndPoints.WATCHLIST) {
                headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                setBody(serializedWatchlistCreation)
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    /**
     * Test the update watchlist endpoint.
     */
    @Test
    fun `update watchlist should return 200 OK`() = testApplication {
        runBlocking {
            val updateWatchlist = UpdateWatchlist(USER_ID_TEST, WATCHLIST_ID_TEST, updatedSymbols)
            val serializedUpdateWatchlist = Json.encodeToString(updateWatchlist)
            val response = client.put(ApiEndPoints.WATCHLIST) {
                headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                setBody(serializedUpdateWatchlist)
            }
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    /**
     * Test retrieving all watchlist for a user.
     */
    @Test
    fun `get all watchlist for user should return 200 OK`() = testApplication {
        runBlocking {
            val response = client.get(ApiEndPoints.WATCHLIST + GET_ALL_WATCHLIST_FOR_USER_TEST)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    /**
     * Test retrieving all watchlists with an invalid UUID.
     */
    @Test
    fun `get all watchlist with invalid UUID should return 400 Bad Request`() = testApplication {
        runBlocking {
            val response = client.get(ApiEndPoints.WATCHLIST + "/invalidUUID")
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    /**
     * Test deleting a watchlist with a valid user and invalid watchlist ID.
     */
    @Test
    fun `delete watchlist with invalid watchlist ID should throw BadRequestException`() = testApplication {

        runBlocking {
            try {
                val userId = testUserId
                val watchlistId = UUID.randomUUID()
                val deleteWatchlist = DeleteWatchlist(userId.toString(), watchlistId.toString())
                val serializedDeleteWatchlist = Json.encodeToString(deleteWatchlist)
                client.delete(ApiEndPoints.WATCHLIST) {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedDeleteWatchlist)
                }
            } catch (e: BadRequestException) {
                assertEquals("Watchlist not found or already deleted", e.message)
            }
        }
    }
}
