package com.example.dao

import com.example.model.request.User
import com.example.model.response.UserResponse

interface UserDAO {
    suspend fun createUser(userName: String, password: String): UserResponse?
    suspend fun getAllUser(): List<User>?
}
