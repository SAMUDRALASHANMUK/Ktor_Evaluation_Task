import com.example.database.table.RecentWatchList
import com.example.database.table.Users
import com.example.database.table.Watchlists
import com.example.model.request.Symbol
import com.example.repository.RecentWatchlistDAOImpl
import com.example.repository.WatchlistDAOImpl
import com.example.utils.TestDatabase
import com.example.utils.testhelpers.createUserForTesting
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.testng.Assert.assertFalse
import java.sql.Connection
import java.util.*
import kotlin.test.assertEquals

/**
 * Unit tests for the Recent Watchlist Data Access Object (DAO) implementation.
 */
class RecentWatchlistDAOImplTest {
    private val recentWatchlistDAOImpl = RecentWatchlistDAOImpl()
    private lateinit var database: org.jetbrains.exposed.sql.Database
    private lateinit var testUserId: UUID
    private val watchlistDAOImpl = WatchlistDAOImpl()

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
     * Test retrieving the recent watchlist for a user when no watchlist exists.
     * Expecting a negative response with no recent watchlist.
     */
    @Test
    fun `test Get Recent Watchlist - No Watchlist Exist `() = testApplication {
        val userId = testUserId
        val response = recentWatchlistDAOImpl.getRecentWatchlist(userId)
        assertFalse(response?.success == true)
        assertTrue(response?.recentWatchlistId == null)
    }

    /**
     * Test retrieving the recent watchlist for a user when a watchlist with symbols exists.
     * Expecting a positive response with the recent watchlist data.
     */
    @Test
    fun `test Get Recent Watchlist `() = testApplication {
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

        watchlistDAOImpl.createWatchlist(userId, symbolsToAdd)
        val response = recentWatchlistDAOImpl.getRecentWatchlist(userId)
        assertTrue(response?.success == true)
        if (response != null) {
            assertEquals("Recent watchlist retrieved successfully", response.message)
        }
    }
}

