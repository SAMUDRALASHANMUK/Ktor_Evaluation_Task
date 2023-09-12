package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistration(
    val username: String,
    val password: String
)
