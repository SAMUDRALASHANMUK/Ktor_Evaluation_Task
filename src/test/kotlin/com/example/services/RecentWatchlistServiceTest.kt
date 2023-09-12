package com.example.services

import com.example.database.table.RecentWatchList
import com.example.database.table.Users
import com.example.database.table.Watchlists
import com.example.di.appModule
import com.example.model.request.Symbol
import com.example.utils.TestDatabase
import com.example.utils.testhelpers.createUserForTesting
import io.ktor.server.plugins.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.fail


class RecentWatchlistServiceTest {
    private val recentWatchlistService = RecentWatchlistService()
    private lateinit var database: org.jetbrains.exposed.sql.Database
    private lateinit var testUserId: UUID
    private val watchlistService = WatchlistService()

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
     * Test retrieving the recent watchlist for a user with an existing watchlist.
     * Expecting a successful response.
     */
    @Test
    fun `test Get Recent Watchlist Positive`() = runBlocking {
        val userId = testUserId
        val symbolsToAdd = listOf(
            Symbol(
                asset = "AAPL",
                strike = 150.0,
                lotSize = 100,
                tickSize = 0.01,
                streamSym = "AAPL",
                instrument = "Stock",
                multiplier = 1.0,
                displayName = "Apple Inc.",
                companyName = "Apple Inc.",
                expiry = "",
                optionChain = false,
                symbolTag = "Tech",
                sector = "Technology",
                exchange = "NASDAQ",
                isIn = "USA",
                baseSymbol = "AAPL"
            )
        )

        watchlistService.createWatchlist(userId, symbolsToAdd)
        val response = recentWatchlistService.getRecentWatchlist(userId)
        org.junit.Assert.assertTrue(response.success)
        assertEquals("Recent watchlist retrieved successfully", response.message)
    }

    /**
     * Test for the scenario where no recent watchlist is found for the user,
     * and a BadRequestException is expected to be thrown.
     */

    @Test
    fun `test Get Recent Watchlist - No Recent Watchlist`() = runBlocking {
        val userId = testUserId
        try {
            recentWatchlistService.getRecentWatchlist(userId)
            fail("Expected a BadRequestException but didn't get one.")
        } catch (e: BadRequestException) {
            assertEquals("No recent watchlist found for the user", e.localizedMessage)
        }
    }
}
