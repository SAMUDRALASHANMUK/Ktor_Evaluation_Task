package com.example.repository

import com.example.database.table.Users
import com.example.model.response.UserResponse
import com.example.utils.TestDatabase
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import kotlin.test.assertEquals

/**
 * Unit tests for the User Data Access Object (DAO) implementation.
 */
class UserDAOImplTest {

    private val userDAO = UserDAOImpl()


    private lateinit var database: org.jetbrains.exposed.sql.Database

    @Before
    fun setup() {
        database = TestDatabase.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Users)
        }
    }

    /**
     * Test retrieving all users when no users exist in the database.
     * Expecting an empty list of users.
     */
    @Test
    fun `test Get All Users - No Users Exist - Success`() = runBlocking {
        val userList = userDAO.getAllUser()
        assertEquals(emptyList(), userList)
    }

    /**
     * Test creating a user with valid credentials.
     * Expecting a successful user creation response.
     */
    @Test
    fun `test Create User - Success`() = runBlocking {
        val userName = "shanmukrao"
        val password = "shanmukshanmuk12"

        val newUserResponse: UserResponse? = userDAO.createUser(userName, password)

        assertEquals(true, newUserResponse?.success)
        assertEquals("User created successfully", newUserResponse?.message)
    }
}
