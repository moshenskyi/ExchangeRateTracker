package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.ExchangeRateRepositoryImpl
import javax.inject.Inject

class RemoveFavoriteItemUseCase @Inject constructor(
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl
) {
    suspend fun execute(ticker: String) {
        exchangeRateRepositoryImpl.removeFavorite(ticker)
    }
}