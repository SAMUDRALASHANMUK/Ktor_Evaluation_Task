package com.example.repository

import com.example.exception.CommonException
import com.example.dao.WatchlistDAO
import com.example.database.table.RecentWatchList
import com.example.database.table.Watchlists
import com.example.entity.WatchListEntity
import com.example.model.request.Symbol
import com.example.model.response.Watchlist
import com.example.model.response.WatchlistResponse
import com.example.util.helperfunctions.toWatchlistEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID
import io.ktor.util.logging.Logger
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory

/**
 * Implementation of the WatchlistDAO interface for watchlist-related database operations.
 */
class WatchlistDAOImpl : WatchlistDAO {
    private val logger: Logger = LoggerFactory.getLogger(WatchlistDAOImpl::class.java)

    /**
     * Create a new watchlist for a user with the provided symbols.
     *
     * @param userId The ID of the user who owns the watchlist.
     * @param symbols The symbols to associate with the watchlist.
     * @return A [WatchlistResponse] indicating the result of the operation.
     */
    override fun createWatchlist(userId: UUID, symbols: List<Symbol>): WatchlistResponse {
        logger.info("Creating a new watchlist for user with ID: $userId")
        return try {
            transaction {
                val symbolsJson = Json.encodeToString(symbols)
                val watchlistId = Watchlists.insertAndGetId {
                    it[Watchlists.userId] = userId
                    it[isDelete] = false
                    it[createdAt] = Instant.now().toString()
                    it[updatedAt] = Instant.now().toString()
                    it[Watchlists.symbols] = symbolsJson
                }

                run {
                    RecentWatchList.insert {
                        it[RecentWatchList.watchlistId] = watchlistId.value
                        it[RecentWatchList.userId] = userId
                    }
                    logger.info("Created a new watchlist for user with ID: $userId")
                    WatchlistResponse(
                        success = true, message = "Watchlist created successfully"
                    )

                }
            }
        } catch (e: ExposedSQLException) {
            logger.error("Failed to create a new watchlist for user with ID: $userId - ${e.message}", e)
            throw CommonException(
                success = false,
                message = "Failed to create watchlist"
            )
        }
    }

    /**
     * Delete a watchlist with the specified ID for the given user.
     *
     * @param userId The ID of the user who owns the watchlist.
     * @param watchlistId The ID of the watchlist to delete.
     * @return A [WatchlistResponse] indicating the result of the watchlist deletion.
     */
    override fun deleteWatchlist(userId: UUID, watchlistId: UUID): WatchlistResponse {
        logger.info("Deleting watchlist for user with ID: $userId, watchlist ID: $watchlistId")
        return try {
            val deletedWatchlist = transaction {
                Watchlists.select { Watchlists.id eq watchlistId and (Watchlists.userId eq userId) }.singleOrNull()
            }

            if (deletedWatchlist != null) {
                transaction {
                    Watchlists.update({ Watchlists.id eq watchlistId and (Watchlists.userId eq userId) }) {
                        it[isDelete] = true
                    }
                    RecentWatchList.deleteWhere {
                        RecentWatchList.watchlistId eq watchlistId
                    }
                }
                logger.info("Deleted watchlist for user with ID: $userId, watchlist ID: $watchlistId")
                WatchlistResponse(
                    success = true, message = "Watchlist deleted successfully"
                )
            } else {
                logger.warn("Watchlist not found or already deleted: User ID: $userId, Watchlist ID: $watchlistId")
                WatchlistResponse(
                    success = false, message = "Watchlist not found or already deleted"
                )
            }
        } catch (e: PSQLException) {
            logger.error(
                "Failed to delete watchlist for user with ID: $userId, watchlist ID: $watchlistId - ${e.message}",
                e
            )
            throw CommonException(
                success = false, message = "Failed to delete watchlist"
            )
        }
    }

    /**
     * Update a watchlist with the specified ID for the given user with the provided symbol information.
     *
     * @param userId The ID of the user who owns the watchlist.
     * @param watchlistId The ID of the watchlist to update.
     * @param input The symbol information to update the watchlist.
     * @return A [WatchlistResponse] indicating the result of the watchlist update.
     */
    override suspend fun updateWatchlist(userId: UUID, watchlistId: UUID, input: List<Symbol>): WatchlistResponse {
        logger.info("Updating watchlist for user with ID: $userId, watchlist ID: $watchlistId")
        return try {
            transaction {

                val symbolsJson = Json.encodeToString(input)

                val updatedCount = Watchlists.update({
                    (Watchlists.id eq watchlistId) and (Watchlists.userId eq userId)
                }) {
                    it[symbols] = symbolsJson
                    it[updatedAt] = Instant.now().toString()
                }

                if (updatedCount > 0) {
                    logger.info("Updated watchlist for user with ID: $userId, watchlist ID: $watchlistId")
                    WatchlistResponse(
                        success = true,
                        message = "Symbols within the watchlist updated successfully"
                    )
                } else {
                    logger.warn("Watchlist or symbols not updated for User ID: $userId, Watchlist ID: $watchlistId")
                    WatchlistResponse(
                        success = false,
                        message = "Watchlist not found or symbols not updated"
                    )
                }
            }
        } catch (e: PSQLException) {
            logger.error(
                "Failed to update symbols in the watchlist for User ID: $userId, Watchlist ID: $watchlistId",
                e
            )
            throw CommonException(
                success = false, message = "Failed to update watchlist"
            )
        }
    }


    /**
     * Retrieves all non-deleted watchlist associated with the specified user.
     *
     * @param userId The UUID of the user for whom watchlist are to be retrieved.
     * @return A list of [Watchlist] objects containing information about the retrieved watchlist.
     * @throws CommonException if there is an error while fetching watchlist or if the user does not exist.
     */
    override fun getAllWatchlist(userId: UUID): List<Watchlist> {
        return try {
            transaction {
                val watchlist = WatchListEntity.find {
                    (Watchlists.userId eq userId) and (Watchlists.isDelete eq false)
                }.map { toWatchlistEntity(it) }
                logger.info("Fetched watchlist for user with ID: $userId")
                watchlist
            }
        } catch (e: PSQLException) {
            logger.error("Failed to fetch watchlist for user with ID: $userId - ${e.message}", e)
            throw CommonException(
                success = false, message = "Failed to fetch watchlist for user"
            )
        }
    }
}

