package com.example.services

import com.example.model.request.Symbol
import com.example.model.response.Watchlist
import com.example.model.response.WatchlistResponse
import com.example.repository.WatchlistDAOImpl
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.util.logging.KtorSimpleLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

/**
 * Service layer for managing watchlist.
 */
class WatchlistService : KoinComponent {

    // Inject the WatchlistDAO implementation
    private val watchlistDAOImpl by inject<WatchlistDAOImpl>()
    private val logger = KtorSimpleLogger("com.example.services.WatchlistService")

    /**
     * Creates a watchlist for the specified user with the given symbols.
     *
     * @param userId The UUID of the user for whom the watchlist is created.
     * @param symbols The list of symbols to be added to the watchlist.
     * @return A [WatchlistResponse] containing information about the watchlist creation.
     * @throws BadRequestException if the watchlist creation fails, with an error message indicating the cause.
     */
    fun createWatchlist(userId: UUID, symbols: List<Symbol>): WatchlistResponse {
        val response = watchlistDAOImpl.createWatchlist(userId, symbols)

        if (response.success) {
            logger.info("Created watchlist for user ID: $userId")

            return response
        } else {
            val errorMessage = response.message ?: "Watchlist creation failed"
            logger.error("Watchlist creation failed: $errorMessage")
            throw BadRequestException(errorMessage)
        }
    }

    /**
     * Deletes a watchlist associated with the specified user and watchlist ID.
     *
     * @param userId The UUID of the user who owns the watchlist.
     * @param watchlistId The UUID of the watchlist to be deleted.
     * @return A [WatchlistResponse] containing information about the watchlist deletion.
     * @throws BadRequestException if the watchlist deletion fails, with an error message indicating the cause.
     */
    fun deleteWatchlist(userId: UUID, watchlistId: UUID): WatchlistResponse {
        val response = watchlistDAOImpl.deleteWatchlist(userId, watchlistId)
        if (response.success) {
            logger.info("Deleted watchlist ID: $watchlistId for user ID: $userId")
            return response
        } else {
            val errorMessage = response.message ?: "Watchlist deletion failed"
            logger.error("Watchlist deletion failed: $errorMessage")
            throw BadRequestException(errorMessage)
        }
    }

    /**
     * Updates a watchlist associated with the specified user and watchlist ID with the provided list of symbols.
     *
     * @param userId The UUID of the user who owns the watchlist.
     * @param watchlistId The UUID of the watchlist to be updated.
     * @param input The list of [Symbol] objects to update the watchlist with.
     * @return A [WatchlistResponse] containing information about the watchlist update.
     * @throws BadRequestException if the watchlist update fails, with an error message indicating the cause.
     */
    suspend fun updateWatchlist(userId: UUID, watchlistId: UUID, input: List<Symbol>): WatchlistResponse {
        val response = watchlistDAOImpl.updateWatchlist(userId, watchlistId, input)
        if (response.success) {
            logger.info("Updated watchlist ID: $watchlistId for user ID: $userId")

            return response
        } else {
            val errorMessage = response.message ?: "Watchlist update failed"
            logger.error("Watchlist update failed: $errorMessage")
            throw BadRequestException(errorMessage)
        }
    }

    /**
     * Retrieves a list of watchlist associated with the specified user.
     *
     * @param userId The UUID of the user for whom watchlist are retrieved.
     * @return A list of [Watchlist] objects representing the user's watchlist.
     * @throws NotFoundException if no watchlist are found for the user.
     */
    fun getAllWatchlist(userId: UUID): List<Watchlist> {
        val watchlist = watchlistDAOImpl.getAllWatchlist(userId)
        if (watchlist.isNotEmpty()) {
            logger.info("Retrieved ${watchlist.size} watchlists for user ID: $userId")
            return watchlist
        } else {
            logger.warn("No watchlist found for user ID: $userId")
            throw NotFoundException("No watchlist found")
        }
    }
}
