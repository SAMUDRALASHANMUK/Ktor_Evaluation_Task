package com.example.services

import com.example.database.table.RecentWatchList
import com.example.database.table.Users
import com.example.database.table.Watchlists
import com.example.di.appModule
import com.example.exception.CommonException
import com.example.utils.TestDatabase
import com.example.utils.appconstants.GlobalConstants
import com.example.utils.testhelpers.createUserForTesting
import com.example.utils.testhelpers.getWatchlistIdByUserId
import io.ktor.server.plugins.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.testng.AssertJUnit
import java.sql.Connection
import java.util.*
import org.junit.Assert.*
import org.junit.Test


class WatchlistServiceTest {
    private val watchlistService = WatchlistService()
    private lateinit var database: org.jetbrains.exposed.sql.Database
    private lateinit var testUserId: UUID

    @Before
    fun setup() {
        startKoin {
            modules(appModule)
        }
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
     * Test adding symbols to a user's watchlist with valid symbols, expecting a successful response.
     */

    @Test
    fun `test Add Symbols To Watchlist With Valid Symbols`() = runBlocking {

        val userId = testUserId
        val response = watchlistService.createWatchlist(userId, GlobalConstants.symbolsToAdd)
        assertEquals("Watchlist created successfully", response.message)
    }

    /**
     * Test removing symbols from a non-existent watchlist, expecting a failure response.
     */
    @Test
    fun `test Remove Symbols From Non-Existent Watchlist`(): Unit = runBlocking {
        val userId = UUID.randomUUID()
        val watchlistId = UUID.randomUUID()
        try {
            watchlistService.deleteWatchlist(userId, watchlistId)
        } catch (e: BadRequestException) {
            assertEquals("Watchlist not found or already deleted", e.message)
        }
    }

    /**
     * Test scenario: Updating a user's watchlist with new symbols, expecting a positive response.
     */
    @Test
    fun `test Update Watchlist With Valid Symbols`() = runBlocking {
        val userId = testUserId
        watchlistService.createWatchlist(userId, GlobalConstants.symbolsToAdd)
        val watchlistId = getWatchlistIdByUserId(userId)
        val updateResponse = watchlistService.updateWatchlist(userId, watchlistId, GlobalConstants.updatedSymbols)
        AssertJUnit.assertTrue(updateResponse.success)
    }

    /**
     * Test scenario: Retrieving watchlist for a user with no existing watchlist.
     */
    @Test
    fun `test Get AllWatchlist For User With NoExisting Watchlist`() {
        val userId = UUID.randomUUID()
        try {
            watchlistService.getAllWatchlist(userId)
        } catch (e: NotFoundException) {
            assertEquals("No watchlist found", e.message)
        }
    }

    /**
     * Tests the scenario where retrieving all watchlist for a user with existing watchlist should succeed.
     * The test ensures that a non-empty list of watchlist is returned for the user.
     */
    @Test
    fun `test get All watchlist for user`() {
        val userId = testUserId
        watchlistService.createWatchlist(userId, GlobalConstants.symbolsToAdd)
        val watchlist = watchlistService.getAllWatchlist(userId)
        AssertJUnit.assertTrue(watchlist.isNotEmpty())
    }

    /**
     * Tests the scenario where updating a watchlist with invalid symbols or an invalid watchlist ID
     * results in a negative response. The test ensures that the response indicates failure
     * and includes the appropriate error message.
     */
    @Test
    fun `test UpdateWatchlist InvalidInput Failure`(): Unit = runBlocking {
        val userId = testUserId
        watchlistService.createWatchlist(userId, GlobalConstants.symbolsToAdd)
        try {
            watchlistService.updateWatchlist(userId, UUID.randomUUID(), GlobalConstants.invalidSymbols)
        } catch (e: BadRequestException) {
            assertEquals("Watchlist not found or symbols not updated", e.message)
        }
    }

    /**
     * Tests the scenario where an exception is thrown while attempting to retrieve all watchlist
     * for a user with an invalid user ID. The test should catch the exception and verify its properties.
     */
    @Test
    fun `test Get All Watchlist Exception Thrown For InvalidUser`() {
        testUserId
        val invalidUserId = UUID.randomUUID()
        try {
            watchlistService.getAllWatchlist(invalidUserId)
        } catch (e: NotFoundException) {
            assertEquals("No watchlist found", e.message)
        }
    }

    /**
     * Positive test: Tests the deletion of a watchlist when the deletion is successful.
     * The test should verify that the function returns a success response and logs the deletion.
     */
    @Test
    fun `test Delete Watchlist - Successful Deletion`() = runBlocking {
        val userId = testUserId
        watchlistService.createWatchlist(userId, GlobalConstants.symbolsToAdd)
        val watchlistId = getWatchlistIdByUserId(userId)
        val response = watchlistService.deleteWatchlist(userId, watchlistId)
        assertTrue(response.success)
        assertEquals("Watchlist deleted successfully", response.message)
    }

    /**
     * Test case for [WatchlistService.createWatchlist] to verify that it throws a [CommonException]
     * with the expected error message when watchlist creation fails.
     */
    @Test
    fun `test Create Watchlist - Failed Creation`() = runBlocking {
        val userId = UUID.randomUUID()

        try {
            watchlistService.createWatchlist(userId, GlobalConstants.symbolsToAdd)
            fail("Expected a Common Exception but didn't get one.")
        } catch (e: CommonException) {
            assertEquals("Failed to create watchlist", e.message)
        }
    }
}
