package com.example

import com.example.database.table.DatabaseFactory
import com.example.plugins.configureCallLogging
import com.example.plugins.configureContentNegotiation
import com.example.plugins.configureRequestValidation
import com.example.plugins.configureStatusPages
import com.example.plugins.configureKoin
import com.example.plugins.configureRouting
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.module() {

    // Configure call logging for request/response information
    configureCallLogging()

    // Configure content negotiation for handling different response formats
    configureContentNegotiation()

    // Configure request validation to ensure data integrity
    configureRequestValidation()

    // Configure status pages to handle errors gracefully
    configureStatusPages()

    // Configure Koin for dependency injection
    configureKoin()

    // Initialize the database connection
    DatabaseFactory.init()

    // Configure routing for handling HTTP requests
    configureRouting()
}

