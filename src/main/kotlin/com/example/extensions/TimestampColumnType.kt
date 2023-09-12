package com.example.extensions

import org.jetbrains.exposed.sql.ColumnType
import org.postgresql.util.PGobject
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.util.Calendar
import java.util.TimeZone

class TimestampColumnType : ColumnType() {
    private val timestampWithTimeZone = "timestamp with time zone"
    private val zone = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC))
    override fun sqlType() =
        timestampWithTimeZone

    override fun valueFromDB(value: Any): Any = when (value) {
        is PGobject -> value.value!!
        else -> value.toString()
    }

    override fun valueToString(value: Any?): String = when (value) {
        is Iterable<*> -> nonNullValueToString(value)
        else -> super.valueToString(value)
    }

    override fun notNullValueToDB(value: Any): Any {
        return Timestamp.from(Instant.now())
    }

    override fun readObject(rs: ResultSet, index: Int): Any? {
        return rs.getTimestamp(index, zone)
    }
}
