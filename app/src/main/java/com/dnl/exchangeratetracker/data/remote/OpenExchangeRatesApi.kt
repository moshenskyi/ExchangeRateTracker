package com.dnl.exchangeratetracker.data.remote

import com.dnl.exchangeratetracker.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenExchangeRatesApi {
    @GET("latest.json")
    suspend fun getLatestRates(@Query("app_id") appId: String = BuildConfig.APP_ID): ExchangeRateResponse
}