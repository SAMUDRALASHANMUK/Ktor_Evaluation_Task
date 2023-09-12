package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RecentWatchlist(
    val id: String,
    val watchListId: String,
    val userId: String
)
