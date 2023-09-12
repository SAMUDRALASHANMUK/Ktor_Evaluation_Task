package com.example.dao

import com.example.model.response.GetRecentWatchlistResponse
import java.util.UUID

interface RecentWatchlistDAO {
    suspend fun getRecentWatchlist(userId: UUID): GetRecentWatchlistResponse?
}
