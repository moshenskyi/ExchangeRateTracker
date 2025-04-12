package com.dnl.exchangeratetracker.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

private const val APP_ID = "34ad304336e24c20acf030ff4958925b"

interface OpenExchangeRatesApi {
    @GET("latest.json")
    suspend fun getLatestRates(@Query("app_id") appId: String = APP_ID): ExchangeRateResponse
}