package com.example.com.example.helperfunctions

import com.example.extensions.JsonbColumnType
import org.junit.Test
import org.postgresql.util.PGobject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JsonbColumnTypeTest {

    @Test
    fun testValueFromDBWithPGobject() {
        val columnType = JsonbColumnType()
        val pgObject = PGobject()
        pgObject.type = "jsonb"
        pgObject.value = "{\"key\":\"value\"}"

        val result = columnType.valueFromDB(pgObject)

        assertNotNull(result)
        assertTrue(result is String)
        assertEquals("{\"key\":\"value\"}", result)
    }

    @Test
    fun testValueFromDBWithNonPGobject() {
        val columnType = JsonbColumnType()
        val value = "{\"key\":\"value\"}"

        val result = columnType.valueFromDB(value)

        assertNotNull(result)
        assertTrue(result is String)
        assertEquals("{\"key\":\"value\"}", result)
    }
}
