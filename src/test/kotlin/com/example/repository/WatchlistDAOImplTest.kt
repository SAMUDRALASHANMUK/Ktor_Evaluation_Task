package com.example.repository

import com.example.database.table.RecentWatchList
import com.example.database.table.Users
import com.example.database.table.Watchlists
import com.example.exception.CommonException
import com.example.utils.TestDatabase
import com.example.utils.appconstants.GlobalConstants.invalidSymbols
import com.example.utils.appconstants.GlobalConstants.symbolsToAdd
import com.example.utils.appconstants.GlobalConstants.updatedSymbols
import com.example.utils.testhelpers.createUserForTesting
import com.example.utils.testhelpers.getWatchlistIdByUserId
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.testng.AssertJUnit.assertTrue
import java.sql.Connection
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Unit tests for the Watchlist Data Access Object (DAO) implementation.
 */
class WatchlistDAOImplTest {
    private val watchlistDAOImpl = WatchlistDAOImpl()
    private lateinit var testUserId: UUID

    private lateinit var database: org.jetbrains.exposed.sql.Database

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
        transaction(database) {
            SchemaUtils.drop(Users, Watchlists, RecentWatchList)
        }
    }

    /**
     * Test adding symbols to a watchlist with positive input.
     * Expecting a successful watchlist creation response.
     */
    @Test
    fun `test Add Symbols To Watchlist `() = testApplication {

        val userId = testUserId

        val response = watchlistDAOImpl.createWatchlist(userId, symbolsToAdd)
        assertEquals("Watchlist created successfully", response.message)
    }

    /**
     * Test removing symbols from a non-existent watchlist.
     * Expecting a failure response.
     */
    @Test
    fun `test Remove Symbols From Watchlist - Non-Existent Watchlist`() {
        val userId = UUID.randomUUID()
        val watchlistId = UUID.randomUUID()

        val response = watchlistDAOImpl.deleteWatchlist(userId, watchlistId)

        assertFalse(response.success)
        assertEquals("Watchlist not found or already deleted", response.message)
    }

    /**
     * Test updating a watchlist with positive input.
     * Expecting a successful watchlist update response.
     */
    @Test
    fun `test Update Watchlist - Positive`() = testApplication {
        val userId = testUserId
        watchlistDAOImpl.createWatchlist(userId, symbolsToAdd)
        val watchlistId = getWatchlistIdByUserId(userId)
        val updateResponse = watchlistDAOImpl.updateWatchlist(userId, watchlistId, updatedSymbols)
        assertTrue(updateResponse.success)
    }

    /**
     * Test retrieving all watchlist for a non-existent user.
     * Expecting an empty list.
     */
    @Test
    fun `test Get All Watchlist - Empty list`() {
        val userId = UUID.randomUUID()

        val watchlist = watchlistDAOImpl.getAllWatchlist(userId)

        assertTrue(watchlist.isEmpty())
    }

    /**
     * Test retrieving all watchlist for a user with existing watchlist.
     * Expecting a non-empty list.
     */
    @Test
    fun `test Get All Watchlist - Positive`() {
        val userId = testUserId
        watchlistDAOImpl.createWatchlist(userId, symbolsToAdd)
        val watchlist = watchlistDAOImpl.getAllWatchlist(userId)
        assertTrue(watchlist.isNotEmpty())
    }

    /**
     * Test updating a watchlist with invalid input.
     * Expecting a failure response.
     */
    @Test
    fun `test Update Watchlist - Failure`() = runBlocking {
        val userId = testUserId
        watchlistDAOImpl.createWatchlist(userId, symbolsToAdd)

        val response = watchlistDAOImpl.updateWatchlist(userId, UUID.randomUUID(), invalidSymbols)
        assertFalse(response.success)
        assertEquals("Watchlist not found or symbols not updated", response.message)
    }

    /**
     * Test retrieving all watchlist for a non-existent user and expecting a CommonException.
     */
    @Test
    fun `test Get All Watchlist - Exception`() {
        testUserId

        val invalidUserId = UUID.randomUUID()

        try {
            watchlistDAOImpl.getAllWatchlist(invalidUserId)
        } catch (e: CommonException) {
            assertFalse(e.success)
            assertEquals("Failed to fetch watchlist for user", e.message)
        }
    }
}
