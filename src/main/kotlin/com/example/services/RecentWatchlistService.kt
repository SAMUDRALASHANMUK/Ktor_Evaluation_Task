package com.example.services

import com.example.model.response.GetRecentWatchlistResponse
import com.example.repository.RecentWatchlistDAOImpl
import io.ktor.server.plugins.BadRequestException
import io.ktor.util.logging.KtorSimpleLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

/**
 * Service layer for managing recent watchlist operations.
 */
class RecentWatchlistService : KoinComponent {

    // Inject the RecentWatchlistDAO implementation
    private val recentWatchlistDAO by inject<RecentWatchlistDAOImpl>()
    private val logger = KtorSimpleLogger("com.example.services.RecentWatchlistService")


    /**
     * Get the most recent watchlist for a user.
     *
     * @param userId The ID of the user to retrieve the recent watchlist for.
     * @return A [GetRecentWatchlistResponse] containing the ID of the most recent watchlist.
     */
    suspend fun getRecentWatchlist(userId: UUID): GetRecentWatchlistResponse {
        val response = recentWatchlistDAO.getRecentWatchlist(userId)

        if (response != null && response.success) {
            logger.info("Retrieved most recent watchlist for user ID: $userId")
            return response
        } else {
            val errorMessage = response?.message ?: "Failed to retrieve recent watchlist"
            logger.error("Failed to retrieve recent watchlist for user ID: $userId - $errorMessage")
            throw BadRequestException(errorMessage)
        }
    }
}

