package com.example.plugins

import com.example.model.request.DeleteWatchlist
import com.example.model.request.RecentWatchlist
import com.example.model.request.UpdateWatchlist
import com.example.model.request.UserRegistration
import com.example.model.request.WatchlistCreation
import com.example.util.helperfunctions.validateDeleteWatchlist
import com.example.util.helperfunctions.validateRecentWatchlist
import com.example.util.helperfunctions.validateUpdateWatchlist
import com.example.util.helperfunctions.validateUserRegistration
import com.example.util.helperfunctions.validateWatchlistCreation
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<DeleteWatchlist> { bodyText ->
            when (val validationResult = validateDeleteWatchlist(bodyText)) {
                is ValidationResult.Invalid -> validationResult
                else -> ValidationResult.Valid
            }
        }
        validate<RecentWatchlist> { bodyText ->
            when (val validationResult = validateRecentWatchlist(bodyText)) {
                is ValidationResult.Invalid -> validationResult
                else -> ValidationResult.Valid
            }
        }
        validate<UpdateWatchlist> { bodyText ->
            when (val validationResult = validateUpdateWatchlist(bodyText)) {
                is ValidationResult.Invalid -> validationResult
                else -> ValidationResult.Valid
            }
        }
        validate<UserRegistration> { bodyText ->
            when (val validationResult = validateUserRegistration(bodyText)) {
                is ValidationResult.Invalid -> validationResult
                else -> ValidationResult.Valid
            }
        }
        validate<WatchlistCreation> { bodyText ->
            when (val validationResult = validateWatchlistCreation(bodyText)) {
                is ValidationResult.Invalid -> validationResult
                else -> ValidationResult.Valid
            }
        }
    }
}
