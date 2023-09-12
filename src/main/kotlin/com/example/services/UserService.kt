package com.example.services

import com.example.model.request.User
import com.example.model.response.UserResponse
import com.example.repository.UserDAOImpl
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.util.logging.KtorSimpleLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


/**
 * Service layer for managing user-related operations.
 */
class UserService : KoinComponent {

    // Inject the UserDAO implementation
    private val usersRepository by inject<UserDAOImpl>()

    private val logger = KtorSimpleLogger("com.example.services.UserService")

    /**
     * Get a list of all users.
     *
     * @return A list of [User] objects representing all users.
     *
     * @throws NotFoundException if no users are found.
     */
    suspend fun getAllUsers(): List<User> {
        val users = usersRepository.getAllUser()
        if (users.isNotEmpty()) {
            logger.info("Retrieved ${users.size} users")
            return users
        } else {
            logger.warn("No users found")
            throw NotFoundException("No users found")
        }
    }

    /**
     * Create a new user with the given username and password.
     *
     * @param userName The username of the new user.
     * @param password The password of the new user.
     * @return A [UserResponse] indicating the result of the user creation.
     *
     * @throws BadRequestException if the user creation fails.
     */
    suspend fun createUser(userName: String, password: String): UserResponse {
        val response = usersRepository.createUser(userName, password)

        return if (response?.success == true) {
            logger.info("Created user with ID")
            response
        } else {
            val errorMessage = response?.message ?: "User creation failed"
            logger.error("User creation failed: $errorMessage")
            throw BadRequestException(errorMessage)
        }
    }
}
