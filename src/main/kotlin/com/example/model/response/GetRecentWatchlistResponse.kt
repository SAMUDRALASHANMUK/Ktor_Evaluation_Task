package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetRecentWatchlistResponse(
    val success: Boolean,
    val message: String,
    val recentWatchlistId: String?
)
