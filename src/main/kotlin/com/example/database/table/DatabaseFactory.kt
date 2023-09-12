package com.example.database.table


import com.example.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {
    fun init() {
        Database.connect(
            url = DatabaseConfig.url,
            driver = DatabaseConfig.driver,
            user = DatabaseConfig.user,
            password = DatabaseConfig.password
        )

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users, Watchlists, RecentWatchList)
        }
    }
}


