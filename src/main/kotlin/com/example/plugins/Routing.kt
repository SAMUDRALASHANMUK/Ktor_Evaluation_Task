package com.example.plugins

import com.example.routes.configureRecentWatchlistRoutes
import com.example.routes.configureUserRoutes
import com.example.routes.configureWatchlistRoutes
import io.ktor.server.application.Application


fun Application.configureRouting() {
    configureUserRoutes()
    configureRecentWatchlistRoutes()
    configureWatchlistRoutes()
}
