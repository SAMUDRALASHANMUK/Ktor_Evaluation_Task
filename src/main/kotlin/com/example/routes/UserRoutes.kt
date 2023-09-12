package com.example.routes

import com.example.model.request.UserRegistration
import com.example.services.UserService
import com.example.util.appconstants.ApiEndPoints.USER
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

/**
 * Configures the user-related routes for this Ktor application.
 */
fun Application.configureUserRoutes() {
    val userService by inject<UserService>()

    routing {
        route(USER) {
            /**
             * Handles GET requests to retrieve a list of all users.
             * Responds with HTTP status code 200 if successful.
             */
            get {
                call.application.environment.log.info("Received GET request to retrieve all users")
                val users = userService.getAllUsers()

                // Log message for the GET request
                call.application.environment.log.info("Retrieved ${users.size} users")

                call.respond(users)
            }

            /**
             * Handles POST requests to create a new user.
             * Expects a JSON payload with user data in the request body.
             * Responds with HTTP status code 201 if successful.
             */
            post {
                val user = call.receive<UserRegistration>()

                // Log message for the POST request
                call.application.environment.log.info("POST request to create a user with username: ${user.username}")
                val createdUser = userService.createUser(user.username, user.password)
                call.respond(HttpStatusCode.Created, createdUser)
            }
        }
    }
}
