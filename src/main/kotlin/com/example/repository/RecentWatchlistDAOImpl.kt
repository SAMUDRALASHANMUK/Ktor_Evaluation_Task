package com.example.repository

import com.example.exception.CommonException
import com.example.dao.RecentWatchlistDAO
import com.example.database.table.Watchlists
import com.example.model.response.GetRecentWatchlistResponse
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.util.UUID

/**
 * Implementation of the RecentWatchlistDAO interface that interacts with the database to retrieve
 * the most recent watchlist for a user.
 */
class RecentWatchlistDAOImpl : RecentWatchlistDAO {
    private val logger = LoggerFactory.getLogger(RecentWatchlistDAOImpl::class.java)

    /**
     * Retrieve the most recent watchlist for a user from the database.
     *
     * @param userId The ID of the user for whom to retrieve the recent watchlist.
     * @return A [GetRecentWatchlistResponse] indicating the result of the operation.
     */
    override suspend fun getRecentWatchlist(userId: UUID): GetRecentWatchlistResponse? {
        logger.info("Attempting to retrieve the most recent watchlist for user: $userId")
        return try {
            transaction {
                val mostRecentWatchlistId = Watchlists
                    .slice(Watchlists.id)
                    .select { (Watchlists.userId eq userId) and (Watchlists.isDelete eq false) }
                    .orderBy(Watchlists.createdAt to SortOrder.DESC)
                    .limit(1)
                    .singleOrNull()

                if (mostRecentWatchlistId != null) {
                    logger.info("Retrieved the most recent watchlist for user: $userId")
                    GetRecentWatchlistResponse(
                        success = true,
                        message = "Recent watchlist retrieved successfully",
                        recentWatchlistId = mostRecentWatchlistId[Watchlists.id].value.toString()
                    )
                } else {
                    logger.info("No recent watchlist found for user: $userId")
                    GetRecentWatchlistResponse(
                        success = false,
                        message = "No recent watchlist found for the user",
                        recentWatchlistId = null
                    )
                }
            }
        } catch (ex: ExposedSQLException) {

            logger.error("Failed to retrieve recent watchlist for user: $userId - ${ex.message}")
            throw CommonException(
                success = false,
                message = "Failed to retrieve recent watchlist"
            )
        }
    }
}
