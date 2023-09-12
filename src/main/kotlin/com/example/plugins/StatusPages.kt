package com.example.plugins

import com.example.exception.CommonException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {

                is ExposedSQLException -> call.respondText(
                    "Data Base Error: ${cause.message}",
                    status = HttpStatusCode.BadRequest
                )

                is RequestValidationException ->
                    call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())

                is NotFoundException -> call.respondText(
                    "${cause.message}", status = HttpStatusCode.NoContent
                )

                is CommonException -> call.respondText(
                    "${cause.message}", status = HttpStatusCode.BadRequest
                )

                is BadRequestException -> call.respondText(
                    "${cause.message}", status = HttpStatusCode.BadRequest
                )

            }
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(
                "Oops! It seems the page you're looking for cannot be found.",
                status = status
            )
        }
    }
}
