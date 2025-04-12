package com.dnl.exchangeratetracker.data.remote

import com.dnl.exchangeratetracker.domain.Rate
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponse(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>,
)

fun ExchangeRateResponse.toRates(): List<Rate> = rates.map { (name, value) ->
    Rate(name, value.toString())
}
