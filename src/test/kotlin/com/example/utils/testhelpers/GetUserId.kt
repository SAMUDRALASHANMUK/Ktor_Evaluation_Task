package com.example.utils.testhelpers


import com.example.database.table.Users
import com.example.utils.TestDatabase
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

/**
 * Retrieves the user ID associated with the given username and password.
 *
 * This function queries the database to find the user with the provided username and password,
 * returning the user's UUID if found.
 *
 * @param username The username of the user to look up.
 * @param password The password of the user to look up.
 * @return The UUID of the user if found, or throws a NoSuchElementException if not found.
 * @throws NoSuchElementException if no user is found for the provided username and password.
 */
fun getUserIdByUsernameAndPassword(username: String, password: String): UUID {
    return transaction(TestDatabase.init()) {
        Users
            .slice(Users.id)
            .select { Users.userName eq username and (Users.password eq password) }
            .singleOrNull()
            ?.let { row -> UUID.fromString(row[Users.id].value.toString()) }
            ?: throw NoSuchElementException("User not found for username: $username")
    }
}
