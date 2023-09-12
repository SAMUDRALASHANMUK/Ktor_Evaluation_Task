package com.example.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class Watchlist(
    val userId: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

