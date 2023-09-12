package com.example.helperfunctions

import com.example.model.request.DeleteWatchlist
import com.example.model.request.RecentWatchlist
import com.example.util.helperfunctions.validateDeleteWatchlist
import com.example.util.helperfunctions.validateRecentWatchlist
import io.ktor.server.plugins.requestvalidation.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidationTests {

    @Test
    fun testValidateDeleteWatchlistValid() {
        val validDeleteWatchlist = DeleteWatchlist(
            "550e8400-e29b-41d4-a716-446655440000",
            "550e8400-e29b-41d4-a716-446655440000"
        )
        val result = validateDeleteWatchlist(validDeleteWatchlist)
        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun testValidateDeleteWatchlistInvalidUserId() {
        val invalidDeleteWatchlist = DeleteWatchlist(
            "invalidUserId", // Invalid UUID format
            "6dac1585d1fa41068bf639c8a600ebbc"
        )
        val result = validateDeleteWatchlist(invalidDeleteWatchlist)
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun testValidateRecentWatchlistValid() {
        val validRecentWatchlist = RecentWatchlist(
            "550e8400-e29b-41d4-a716-446655440000",
            "550e8400-e29b-41d4-a716-446655440000",
            "550e8400-e29b-41d4-a716-446655440000"
        )
        val result = validateRecentWatchlist(validRecentWatchlist)
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun testValidateRecentWatchlistInvalidUserId() {
        val invalidRecentWatchlist = RecentWatchlist(
            "invalidUserId", // Invalid UUID format
            "6dac1585d1fa41068bf639c8a600ebbc",
            "6dac1585d1fa41068bf639c8a600ebbc"
        )
        val result = validateRecentWatchlist(invalidRecentWatchlist)
        assertTrue(result is ValidationResult.Invalid)
    }
}
