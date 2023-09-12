package com.example.utils

import com.example.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database

object TestDatabase {

    fun init(): Database {

        return Database.connect(
            url = DatabaseConfig.testUrl,
            driver = DatabaseConfig.driver,
            user = DatabaseConfig.user,
            password = DatabaseConfig.password
        )
    }
}
