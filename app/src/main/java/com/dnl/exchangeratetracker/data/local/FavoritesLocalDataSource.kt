package com.dnl.exchangeratetracker.data.local

import com.dnl.exchangeratetracker.data.local.dao.FavoritesDao
import com.dnl.exchangeratetracker.data.local.entity.FavoriteEntity
import com.dnl.exchangeratetracker.domain.Rate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesLocalDataSource @Inject constructor(
    private val favoritesDao: FavoritesDao
) {
    suspend fun addFavorite(rate: Rate) {
        val entity = FavoriteEntity(ticker = rate.ticker, value = rate.value.toDouble())
        favoritesDao.addFavorite(entity)
    }

    fun getFavorites(): Flow<List<Rate>> = favoritesDao.getAll().map { list ->
        list.map { rate ->
            Rate(ticker = rate.ticker, value = rate.value.toString())
        }
    }

    suspend fun removeFavorite(ticker: String) {
        favoritesDao.removeFavorite(ticker)
    }
}