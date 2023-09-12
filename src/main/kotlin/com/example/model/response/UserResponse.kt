package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val success: Boolean,
    val message: String? = null
)
