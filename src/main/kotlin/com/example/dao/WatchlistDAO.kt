package com.example.dao

import com.example.model.request.Symbol
import com.example.model.response.Watchlist
import com.example.model.response.WatchlistResponse
import java.util.UUID

interface WatchlistDAO {

    fun createWatchlist(userId: UUID, symbols: List<Symbol>): WatchlistResponse

    fun deleteWatchlist(userId: UUID, watchlistId: UUID): WatchlistResponse

    suspend fun updateWatchlist(userId: UUID, watchlistId: UUID, input: List<Symbol>): WatchlistResponse

    fun getAllWatchlist(userId: UUID): List<Watchlist>?
}
