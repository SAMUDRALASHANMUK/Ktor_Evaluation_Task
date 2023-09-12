package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    val username: String,
    val password: String
)
