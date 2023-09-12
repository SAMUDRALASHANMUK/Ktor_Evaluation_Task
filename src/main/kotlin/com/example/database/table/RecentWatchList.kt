package com.example.database.table

import com.example.util.appconstants.RecentWatchlistConstants
import org.jetbrains.exposed.dao.id.UUIDTable

object RecentWatchList : UUIDTable(RecentWatchlistConstants.RECENT_WATCHLIST_TABLE_NAME) {
    val watchlistId = reference(RecentWatchlistConstants.WATCHLIST_ID, Watchlists)
    val userId = reference(RecentWatchlistConstants.USER_ID, Users)
}
