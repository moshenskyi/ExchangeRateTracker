package com.dnl.exchangeratetracker.data

import com.dnl.exchangeratetracker.data.local.FavoritesLocalDataSource
import com.dnl.exchangeratetracker.data.local.RatesLocalDataSource
import com.dnl.exchangeratetracker.data.remote.OpenExchangeRatesApi
import com.dnl.exchangeratetracker.data.remote.parseError
import com.dnl.exchangeratetracker.data.remote.toRates
import com.dnl.exchangeratetracker.domain.Rate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val ratesLocalDataSource: RatesLocalDataSource,
    private val favoritesLocalDataSource: FavoritesLocalDataSource,
    private val api: OpenExchangeRatesApi
) {
    fun getAllRates(): Flow<List<Rate>> = flow {
        val rates = try {
            val apiResponse = api.getLatestRates()
            val freshRates = apiResponse.toRates()

            if (freshRates.isNotEmpty()) {
                ratesLocalDataSource.cacheRates(freshRates)
                freshRates
            } else {
                ratesLocalDataSource.getCachedRates()
            }
        } catch (ex: Exception) {
            val cachedRates = ratesLocalDataSource.getCachedRates()
            cachedRates.ifEmpty { parseError(ex) }
        }
        emit(rates)
    }.flowOn(Dispatchers.IO)

    suspend fun addFavorite(rate: Rate) = favoritesLocalDataSource.addFavorite(rate)

    fun getFavorites(): Flow<List<Rate>> = favoritesLocalDataSource.getFavorites()

    suspend fun removeFavorite(ticker: String) = favoritesLocalDataSource.removeFavorite(ticker)
}
