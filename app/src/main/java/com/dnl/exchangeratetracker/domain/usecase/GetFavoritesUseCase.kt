package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.ExchangeRateRepositoryImpl
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl
) {
    fun execute() = exchangeRateRepositoryImpl.getFavorites()
}