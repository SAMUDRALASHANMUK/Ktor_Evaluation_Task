package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class Symbol(
    var asset: String,
    var strike: Double,
    var lotSize: Int,
    val tickSize: Double,
    val streamSym: String,
    val instrument: String,
    val multiplier: Double,
    val displayName: String,
    val companyName: String,
    val expiry: String,
    val optionChain: Boolean,
    val symbolTag: String,
    val sector: String,
    val exchange: String,
    val isIn: String,
    val baseSymbol: String
)
