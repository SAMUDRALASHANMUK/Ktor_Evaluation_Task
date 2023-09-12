package com.example.database.table

import com.example.util.appconstants.WatchlistConstants
import com.example.util.helperfunctions.jsonb
import com.example.util.helperfunctions.timeStamp
import org.jetbrains.exposed.dao.id.UUIDTable

object Watchlists : UUIDTable(WatchlistConstants.WATCHLIST_TABLE_NAME) {
    val userId = reference("userId", Users)
    val isDelete = bool(WatchlistConstants.WATCHLIST_IS_DELETE)
    val createdAt = timeStamp(WatchlistConstants.WATCHLIST_CREATED_AT)
    val updatedAt = timeStamp(WatchlistConstants.WATCHLIST_UPDATED_AT)
    val symbols = jsonb(WatchlistConstants.WATCHLIST_SYMBOLS)
}

