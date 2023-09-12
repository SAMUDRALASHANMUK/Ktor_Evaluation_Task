package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteWatchlist(val userId: String, val watchlistId: String)
