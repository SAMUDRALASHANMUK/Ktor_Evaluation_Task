package com.example.services

import com.example.database.table.Users
import com.example.di.appModule
import com.example.model.response.UserResponse
import com.example.utils.TestDatabase
import io.ktor.server.plugins.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * Unit tests for [UserService].
 */
class UserServiceTest {

    private val userService = UserService()
    private lateinit var database: org.jetbrains.exposed.sql.Database

    @Before
    fun setup() {
        startKoin {
            modules(appModule)
        }
        database = TestDatabase.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Users)
        }

    }

    @After
    fun tearDown() {
        stopKoin()
        transaction(database) {
            SchemaUtils.drop(Users)
        }
    }

    /**
     * Tests creating a user with valid credentials. The test should succeed if the user creation is successful.
     */
    @Test
    fun `test creating a user with valid credentials should succeed`() = runBlocking {
        val userName = "shanmukrao"
        val password = "shanmukshanmuk12"

        val newUserResponse: UserResponse = userService.createUser(userName, password)

        assertEquals(true, newUserResponse.success)
        assertEquals("User created successfully", newUserResponse.message)
    }

    /**
     * Tests retrieving all users when users exist. The test should succeed if users are retrieved successfully.
     */
    @Test
    fun `test retrieving all users when users exist should succeed`() = runBlocking() {
        userService.createUser("userName", "password")
        val userList = userService.getAllUsers()
        assertEquals(false, userList.isEmpty())
    }

    /**
     * Negative test: Tests retrieving all users when no users exist.
     * The test should throw a NotFoundException.
     */
    @Test
    fun `test retrieving all users when no users exist should throwNotFoundException`() = runBlocking {

        try {
            userService.getAllUsers()
            fail("Expected a NotFoundException but didn't get one.")
        } catch (e: NotFoundException) {

            assertEquals("No users found", e.message)
        }
    }
}
