package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.ExchangeRateRepositoryImpl
import com.dnl.exchangeratetracker.domain.Rate
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl
) {
    suspend fun execute(rate: Rate) = exchangeRateRepositoryImpl.addFavorite(rate)
}