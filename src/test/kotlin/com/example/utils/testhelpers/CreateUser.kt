package com.example.utils.testhelpers

import com.example.repository.UserDAOImpl
import kotlinx.coroutines.runBlocking
import java.util.UUID

/**
 * Creates a new user for testing purposes.
 *
 * This function creates a new user with the specified username and password for testing.
 * It first invokes the `createUser` function to add the user to the database and then retrieves
 * the user's UUID using the `getUserIdByUsernameAndPassword` function.
 *
 * @return The UUID of the created user.
 */
fun createUserForTesting(): UUID {
    val userDAO = UserDAOImpl()
    val userName = "testUser"
    val password = "testPassword"
    runBlocking {
        userDAO.createUser(userName, password)
    }
    return getUserIdByUsernameAndPassword(userName, password)
}
