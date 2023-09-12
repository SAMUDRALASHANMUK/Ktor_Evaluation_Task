package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WatchlistResponse(
    val success: Boolean,
    val message: String? = null,
)
