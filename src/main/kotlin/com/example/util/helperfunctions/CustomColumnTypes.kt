package com.example.util.helperfunctions

import com.example.extensions.JsonbColumnType
import com.example.extensions.TimestampColumnType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

fun Table.timeStamp(name: String): Column<String> = registerColumn(name, TimestampColumnType())


fun Table.jsonb(name: String): Column<String> = registerColumn(name, JsonbColumnType())
