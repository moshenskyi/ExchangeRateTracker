package com.dnl.exchangeratetracker.domain

import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    fun getAllRates(): Flow<List<Rate>>

    suspend fun addFavorite(rate: Rate)

    fun getFavorites(): Flow<List<Rate>>

    suspend fun removeFavorite(ticker: String)
}