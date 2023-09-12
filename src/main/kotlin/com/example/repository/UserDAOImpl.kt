package com.example.repository

import com.example.exception.CommonException
import com.example.dao.UserDAO
import com.example.entity.UserEntity
import com.example.model.request.User
import com.example.model.response.UserResponse
import com.example.util.appconstants.GlobalConstants.UUID_LENGTH
import com.example.util.helperfunctions.toUserDetails
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

/**
 * Implementation of the UserDAO interface for user-related database operations.
 */
class UserDAOImpl : UserDAO {
    private val logger = LoggerFactory.getLogger(UserDAOImpl::class.java)

    /**
     * Create a new user with the provided username and password.
     *
     * @param userName The username of the new user.
     * @param password The password of the new user.
     * @return A [UserResponse] indicating the result of the user creation.
     */
    override suspend fun createUser(userName: String, password: String): UserResponse? {
        logger.info("Creating a new user with username: $userName")
        return try {
            val userEntity = transaction {
                UserEntity.new {
                    this.userName = userName
                    this.password = password
                }
            }

            val createdUser = toUserDetails(userEntity)

            if (createdUser.id!!.length == UUID_LENGTH) {
                logger.info("User created successfully with ID: ${createdUser.id}")
                UserResponse(
                    success = true,
                    message = "User created successfully"
                )
            } else {
                logger.error("Failed to create user with username: $userName")
                UserResponse(
                    success = false,
                    message = "Failed to create user"
                )
            }
        } catch (e: ExposedSQLException) {
            logger.error("Failed to create user with username: $userName - ${e.message}")

            throw CommonException(
                success = false, message = "Failed to create user"
            )
        }

    }

    /**
     * Get a list of all users from the database.
     *
     * @return A list of [User] objects representing all users.
     */
    override suspend fun getAllUser(): List<User> {
        return try {
            logger.info("Retrieving a list of all users from the database")
            val userList = transaction {
                UserEntity.all().map { toUserDetails(it) }
            }
            logger.info("Retrieved ${userList.size} users")
            userList
        } catch (e: ExposedSQLException) {
            logger.error("Failed to retrieve users - ${e.message}")

            throw CommonException(
                success = false,
                message = "Failed to retrieve users"
            )
        }
    }
}

