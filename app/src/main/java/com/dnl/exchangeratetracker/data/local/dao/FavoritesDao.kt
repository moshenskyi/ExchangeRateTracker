package com.dnl.exchangeratetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dnl.exchangeratetracker.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("DELETE FROM favorites WHERE ticker = :ticker")
    suspend fun removeFavorite(ticker: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(item: FavoriteEntity)

    @Query("SELECT * FROM favorites")
    fun getAll(): Flow<List<FavoriteEntity>>
}