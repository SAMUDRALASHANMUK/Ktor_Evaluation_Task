package com.example.utils.testhelpers

import com.example.database.table.Watchlists
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

/**
 * Retrieves the watchlist ID associated with the given user ID.
 *
 * This function queries the database to find the watchlist associated with the provided user ID.
 *
 * @param userId The UUID of the user for whom the watchlist ID is to be retrieved.
 * @return The UUID of the associated watchlist if found, or throws a NoSuchElementException if not found.
 * @throws NoSuchElementException if no watchlist is found for the provided user ID.
 */
fun getWatchlistIdByUserId(userId: UUID): UUID {
    return transaction {
        Watchlists
            .select { Watchlists.userId eq userId and (Watchlists.isDelete eq false) }
            .singleOrNull()
            ?.let { row -> UUID.fromString(row[Watchlists.id].value.toString()) }
            ?: throw NoSuchElementException("not found")
    }
}
