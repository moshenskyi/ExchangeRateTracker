package com.dnl.exchangeratetracker.data.local

import com.dnl.exchangeratetracker.data.local.dao.ExchangeRateDao
import com.dnl.exchangeratetracker.data.local.entity.ExchangeRateEntity
import com.dnl.exchangeratetracker.domain.Rate
import javax.inject.Inject

class RatesLocalDataSource @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao
) {

    fun getCachedRates(): List<Rate> = exchangeRateDao.getAll().map { item ->
        Rate(item.ticker, item.value.toString())
    }

    suspend fun cacheRates(rates: List<Rate>) {
        val local = rates.map { rate ->
            ExchangeRateEntity(ticker = rate.ticker, value = rate.value.toDouble())
        }
        exchangeRateDao.insertAll(*local.toTypedArray())
    }

}