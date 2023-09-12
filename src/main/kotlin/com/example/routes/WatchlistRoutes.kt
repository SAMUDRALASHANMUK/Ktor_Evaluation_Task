package com.example.routes

import com.example.model.request.DeleteWatchlist
import com.example.model.request.UpdateWatchlist
import com.example.model.request.WatchlistCreation
import com.example.services.WatchlistService
import com.example.util.appconstants.ApiEndPoints.GET_ALL_WATCHLIST
import com.example.util.appconstants.ApiEndPoints.WATCHLIST
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.delete
import org.koin.ktor.ext.inject
import java.util.UUID

/**
 * Configure the routes for managing watchlist.
 */
fun Application.configureWatchlistRoutes() {

    val watchlistService by inject<WatchlistService>()

    routing {
        route(WATCHLIST) {

            /**
             * Handle the HTTP POST request to create a new watchlist.
             */
            post {
                val request = call.receive<WatchlistCreation>()

                // Log message for the POST request
                call.application.environment.log.info("Received POST request to create watchlist : ${request.userId}")
                call.application.environment.log.debug("Symbols: {}", request.symbols)

                watchlistService.createWatchlist(UUID.fromString(request.userId), request.symbols).apply {
                    call.respond(HttpStatusCode.Created, this)
                }
            }

            /**
             * Handle the HTTP DELETE request to delete an existing watchlist.
             */
            delete {
                val deleteWatchlist = call.receive<DeleteWatchlist>()

                // Log message for the DELETE request
                call.application.environment.log.info("DELETE request to delete a watchlist: ${deleteWatchlist.userId}")
                call.application.environment.log.debug("Watchlist ID to delete: ${deleteWatchlist.watchlistId}")

                watchlistService.deleteWatchlist(
                    UUID.fromString(deleteWatchlist.userId), UUID.fromString(deleteWatchlist.watchlistId)
                ).apply {
                    call.respond(HttpStatusCode.NoContent, this)
                }
            }

            /**
             * Handle the HTTP PUT request to update an existing watchlist.
             */
            put {
                val watchlist = call.receive<UpdateWatchlist>()

                // Log message for the UPDATE request
                call.application.environment.log.info(" PUT request to update a watchlist : ${watchlist.userId}")
                call.application.environment.log.debug("Watchlist ID to update: ${watchlist.watchlistId}")
                call.application.environment.log.debug("New symbols: {}", watchlist.symbols)

                watchlistService.updateWatchlist(
                    UUID.fromString(watchlist.userId), UUID.fromString(watchlist.watchlistId), watchlist.symbols
                ).apply {
                    call.respond(this)
                }
            }

            /**
             * Handles an HTTP GET request to retrieve all non-deleted watchlists associated with a user.
             */
            get(GET_ALL_WATCHLIST) {
                val userID =
                    runCatching { UUID.fromString(call.parameters["userId"]) }.getOrNull()
                        ?: return@get call.respondText(
                            "Please provide a valid user ID",
                            status = HttpStatusCode.BadRequest
                        )
                // Log message for the GET request
                call.application.environment.log.info("Received GET request to retrieve all watchlist : $userID")

                watchlistService.getAllWatchlist(userID).apply {
                    call.respond(this)
                }
            }
        }
    }
}
