package com.dnl.exchangeratetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnl.exchangeratetracker.data.local.dao.ExchangeRateDao
import com.dnl.exchangeratetracker.data.local.dao.FavoritesDao
import com.dnl.exchangeratetracker.data.local.entity.ExchangeRateEntity
import com.dnl.exchangeratetracker.data.local.entity.FavoriteEntity

@Database(entities = [ExchangeRateEntity::class, FavoriteEntity::class], version = 1)
abstract class CurrencyExchangeDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        const val DATABASE_NAME = "currency_exchange"
    }
}
