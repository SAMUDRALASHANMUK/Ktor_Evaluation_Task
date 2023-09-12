package com.example.extensions

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

class JsonbColumnType : ColumnType() {
    override fun sqlType(): String = "jsonb"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        super.setParameter(
            stmt,
            index,
            value.let {
                PGobject().apply {
                    this.type = sqlType()
                    this.value = value as? String
                }
            })
    }

    override fun valueFromDB(value: Any): Any = when (value) {
        is PGobject -> value.value!!
        else -> value
    }

    override fun valueToString(value: Any?): String = when (value) {
        is Iterable<*> -> nonNullValueToString(value)
        else -> super.valueToString(value)
    }

    override fun notNullValueToDB(value: Any) = value
}
