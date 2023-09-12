package com.example.utils.appconstants

import com.example.model.request.Symbol

object GlobalConstants {
    val symbolsToAdd = listOf(
        Symbol(
            asset = "AAPL",
            strike = 150.0,
            lotSize = 100,
            tickSize = 0.01,
            streamSym = "AAPL",
            instrument = "Stock",
            multiplier = 1.0,
            displayName = "Apple Inc.",
            companyName = "Apple Inc.",
            expiry = "ab",
            optionChain = false,
            symbolTag = "Tech",
            sector = "Technology",
            exchange = "NASDAQ",
            isIn = "USA",
            baseSymbol = "AAPL"
        )
    )
    val invalidSymbols = listOf(
        Symbol(
            asset = "GOOG",
            strike = 200.0,
            lotSize = 100,
            tickSize = 0.01,
            streamSym = "GOOG",
            instrument = "Stock",
            multiplier = 1.0,
            displayName = "Alphabet Inc.",
            companyName = "Alphabet Inc.",
            expiry = "",
            optionChain = false,
            symbolTag = "Tech",
            sector = "Technology",
            exchange = "NASDAQ",
            isIn = "USA",
            baseSymbol = "GOOG"
        )
    )
    val updatedSymbols = listOf(
        Symbol(
            asset = "GOOGL",
            strike = 2000.0,
            lotSize = 50,
            tickSize = 0.1,
            streamSym = "GOOGL",
            instrument = "Stock",
            multiplier = 1.0,
            displayName = "Alphabet Inc.",
            companyName = "Alphabet Inc.",
            expiry = "2024",
            optionChain = false,
            symbolTag = "Tech",
            sector = "Technology",
            exchange = "NASDAQ",
            isIn = "USA",
            baseSymbol = "GOOGL"
        )
    )
    const val GET_RECENT_WATCHLIST_TEST = "/e01cbc4b-42e4-4cae-a500-bf9e761dbada"
    const val USER_ID_TEST = "6dac1585-d1fa-4106-8bf6-39c8a600ebbc"
    const val WATCHLIST_ID_TEST = "0c2677f0-718c-4205-8c1a-4303289d1f3a"
    const val GET_ALL_WATCHLIST_FOR_USER_TEST = "/6dac1585-d1fa-4106-8bf6-39c8a600ebbc"
}
