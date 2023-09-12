package com.example.routes

import com.example.services.RecentWatchlistService
import com.example.util.appconstants.ApiEndPoints.GET_RECENT_WATCHLIST
import com.example.util.appconstants.ApiEndPoints.RECENT_WATCHLIST
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.util.UUID

/**
 * Configures the recent watch list-related routes for this Ktor application.
 */

fun Application.configureRecentWatchlistRoutes() {
    val recentWatchlistService by inject<RecentWatchlistService>()
    routing {
        route(RECENT_WATCHLIST) {
            /**
             * Handles an HTTP GET request to retrieve the recent watchlist associated with a user.
             * If a valid user ID is provided as a query parameter, it returns the recent watchlist data.
             * If the user ID is invalid or not provided, it responds with a "Bad Request" status.
             */
            get(GET_RECENT_WATCHLIST) {
                val userID =
                    runCatching { UUID.fromString(call.parameters["userId"]) }.getOrNull()
                        ?: return@get call.respondText(
                            "Please provide a valid user ID",
                            status = HttpStatusCode.BadRequest
                        )

                // Log message for the GET request
                call.application.environment.log.info("Received GET request to retrieve recent watchlist")
                recentWatchlistService.getRecentWatchlist(userID).apply {
                    call.respond(HttpStatusCode.OK, this)
                }
            }
        }
    }
}
