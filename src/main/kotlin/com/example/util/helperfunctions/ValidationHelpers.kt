package com.example.util.helperfunctions

import com.example.model.request.DeleteWatchlist
import com.example.model.request.UpdateWatchlist
import com.example.model.request.RecentWatchlist
import com.example.model.request.UserRegistration
import com.example.model.request.WatchlistCreation
import com.example.model.request.Symbol
import com.example.util.appconstants.GlobalConstants.MAX_USERNAME_LENGTH
import com.example.util.appconstants.GlobalConstants.MIN_LOT_SIZE
import com.example.util.appconstants.GlobalConstants.MIN_MULTIPLIER
import com.example.util.appconstants.GlobalConstants.MIN_PASSWORD_LENGTH
import com.example.util.appconstants.GlobalConstants.MIN_STRIKE
import com.example.util.appconstants.GlobalConstants.MIN_TICK_SIZE
import com.example.util.appconstants.GlobalConstants.MIN_USERNAME_LENGTH
import com.example.util.appconstants.GlobalConstants.UUID_LENGTH
import io.ktor.server.plugins.requestvalidation.ValidationResult

/**
 * Validates a [DeleteWatchlist] object.
 *
 * This function checks the validity of the [DeleteWatchlist] object by ensuring that the UUID strings
 * for [DeleteWatchlist.userId] and [DeleteWatchlist.watchlistId] are of the correct length (32 characters)
 * and match the expected UUID format.
 *
 * @param deleteWatchlist The [DeleteWatchlist] object to validate.
 * @return A [ValidationResult] indicating whether the validation succeeded or failed.
 */
fun validateDeleteWatchlist(deleteWatchlist: DeleteWatchlist): ValidationResult {
    with(deleteWatchlist) {
        val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        return when {
            userId.length != UUID_LENGTH -> ValidationResult.Invalid("userId must be a valid UUID string of length 32")
            watchlistId.length != UUID_LENGTH -> ValidationResult.Invalid(
                "watchlistId must be a valid UUID string of length $UUID_LENGTH")

            !userId.matches(uuidRegex) -> ValidationResult.Invalid("Invalid UUID format for userId")
            !watchlistId.matches(uuidRegex) -> ValidationResult.Invalid("Invalid UUID format for watchlistId")
            else -> ValidationResult.Valid
        }
    }
}

/**
 * Validates a [RecentWatchlist] object.
 *
 * This function checks the validity of the [RecentWatchlist] object by ensuring that the UUID strings
 * for [RecentWatchlist.id], [RecentWatchlist.userId], and [RecentWatchlist.watchListId] are of the correct
 * length (32 characters) and match the expected UUID format.
 *
 * @param recentWatchlist The [RecentWatchlist] object to validate.
 * @return A [ValidationResult] indicating whether the validation succeeded or failed.
 */
fun validateRecentWatchlist(recentWatchlist: RecentWatchlist): ValidationResult {
    with(recentWatchlist) {
        val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        return when {
            id.length != UUID_LENGTH -> ValidationResult.Invalid("id must be a valid UUID string of length 32")
            userId.length != UUID_LENGTH -> ValidationResult.Invalid("userId must be a valid UUID string of length 32")
            watchListId.length != UUID_LENGTH -> ValidationResult.Invalid(
                "watchlistId must be a valid UUID string of length 32")

            !userId.matches(uuidRegex) -> ValidationResult.Invalid("Invalid UUID format for userId")
            !watchListId.matches(uuidRegex) -> ValidationResult.Invalid("Invalid UUID format for watchlistId")
            !id.matches(uuidRegex) -> ValidationResult.Invalid("Invalid UUID format for watchlistId")
            else -> ValidationResult.Valid
        }
    }
}

/**
 * Validates an [UpdateWatchlist] object.
 *
 * This function performs validation on the properties of an [UpdateWatchlist] object to ensure data integrity
 * and correctness. It checks that the [UpdateWatchlist.userId] and [UpdateWatchlist.watchlistId] are not blank,
 * have a valid format (32 characters hexadecimal string), and that the list of [UpdateWatchlist.symbols] is not empty.
 * It also validates various properties within each symbol.
 *
 * @param updateWatchlist The [UpdateWatchlist] object to validate.
 * @return A [ValidationResult] indicating whether the validation succeeded or failed.
 */
fun validateUpdateWatchlist(updateWatchlist: UpdateWatchlist): ValidationResult {
    with(updateWatchlist) {
        val userIdRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        val watchlistIdRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")

        return when {
            userId.isBlank() -> ValidationResult.Invalid("userId must not be blank")
            watchlistId.isBlank() -> ValidationResult.Invalid("watchlistId must not be blank")
            !userId.matches(userIdRegex) -> ValidationResult.Invalid("Invalid userId format")
            !watchlistId.matches(watchlistIdRegex) -> ValidationResult.Invalid("Invalid watchlistId format")
            else -> validateSymbols(updateWatchlist.symbols)
        }
    }
}

/**
 * Validates a [UserRegistration] object.
 *
 * This function performs validation on the properties of a [UserRegistration] object to ensure data integrity
 * and correctness. It checks that the [UserRegistration.username] is not blank, is at least 5 characters long,
 * and does not exceed 50 characters. It also verifies that the [UserRegistration.password] is not blank and
 * is at least 8 characters long.
 *
 * @param userRegistration The [UserRegistration] object to validate.
 * @return A [ValidationResult] indicating whether the validation succeeded or failed.
 */

fun validateUserRegistration(userRegistration: UserRegistration): ValidationResult {
    with(userRegistration) {
        return when {
            username.isBlank() -> ValidationResult.Invalid("Username must not be blank")
            username.length < MIN_USERNAME_LENGTH -> ValidationResult.Invalid(
                "Username must be at least 5 characters long")

            username.length > MAX_USERNAME_LENGTH -> ValidationResult.Invalid("Username must not exceed 50 characters")
            password.isBlank() -> ValidationResult.Invalid("Password must not be blank")
            password.length < MIN_PASSWORD_LENGTH -> ValidationResult.Invalid(
                "Password must be at least 8 characters long")

            else -> ValidationResult.Valid
        }
    }
}

/**
 * Validates a [WatchlistCreation] object.
 *
 * This function performs validation on the properties of a [WatchlistCreation] object to ensure data integrity
 * and correctness for watchlist creation. It checks that the [WatchlistCreation.userId] is not blank and
 * uses the [validateSymbols] function to validate the list of [WatchlistCreation.symbols].
 *
 * @param watchlistCreation The [WatchlistCreation] object to validate.
 * @return A [ValidationResult] indicating whether the validation succeeded or failed.
 */
fun validateWatchlistCreation(watchlistCreation: WatchlistCreation): ValidationResult {
    return when {
        watchlistCreation.userId.isBlank() -> ValidationResult.Invalid("User ID must not be blank")
        else -> validateSymbols(watchlistCreation.symbols)
    }
}

/**
 * Validates a list of [Symbol] objects.
 *
 * This function checks that each [Symbol] in the list meets certain criteria.
 *
 * @param symbols The list of [Symbol] objects to validate.
 * @return A [ValidationResult] indicating whether the validation succeeded or failed.
 */
fun validateSymbols(symbols: List<Symbol>): ValidationResult {
    return when {
        symbols.isEmpty() -> ValidationResult.Invalid("Symbols list must not be empty")
        symbols.any { it.asset.isBlank() } -> ValidationResult.Invalid("Asset in symbols must not be blank")
        symbols.any { it.strike <= MIN_STRIKE } -> ValidationResult.Invalid("Strike in symbols must be greater than 0")
        symbols.any { it.lotSize <= MIN_LOT_SIZE } -> ValidationResult.Invalid("Lot size  must be greater than 0")
        symbols.any { it.tickSize <= MIN_TICK_SIZE } -> ValidationResult.Invalid("Tick size must be greater than 0")

        symbols.any { it.streamSym.isBlank() } -> ValidationResult.Invalid(
            "Stream symbol in symbols must not be blank")

        symbols.any { it.instrument.isBlank() } -> ValidationResult.Invalid(
            "Instrument in symbols must not be blank")

        symbols.any { it.multiplier <= MIN_MULTIPLIER } -> ValidationResult.Invalid(
            "Multiplier in symbols must be greater than 0")

        symbols.any { it.displayName.isBlank() } -> ValidationResult.Invalid(
            "Display name in symbols must not be blank")

        symbols.any { it.companyName.isBlank() } -> ValidationResult.Invalid(
            "Company name in symbols must not be blank")

        symbols.any { it.expiry.isBlank() } -> ValidationResult.Invalid("Expiry in symbols must not be blank")
        symbols.any { it.symbolTag.isBlank() } -> ValidationResult.Invalid("Symbol tag in symbols must not be blank")
        symbols.any { it.sector.isBlank() } -> ValidationResult.Invalid("Sector in symbols must not be blank")
        symbols.any { it.exchange.isBlank() } -> ValidationResult.Invalid("Exchange in symbols must not be blank")
        symbols.any { it.isIn.isBlank() } -> ValidationResult.Invalid("ISIN in symbols must not be blank")
        symbols.any { it.baseSymbol.isBlank() } -> ValidationResult.Invalid("Base symbol in symbols must not be blank")
        else -> ValidationResult.Valid
    }
}
