package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class WatchlistCreation(
    val userId: String, val symbols: List<Symbol>
)
