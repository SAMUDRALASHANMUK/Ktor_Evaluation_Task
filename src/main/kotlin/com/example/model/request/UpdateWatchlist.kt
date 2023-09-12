package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateWatchlist(
    val userId: String,
    val watchlistId: String,
    val symbols: List<Symbol>
)
