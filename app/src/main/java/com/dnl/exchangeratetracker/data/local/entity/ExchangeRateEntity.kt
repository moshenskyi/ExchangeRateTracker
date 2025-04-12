package com.dnl.exchangeratetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates")
data class ExchangeRateEntity(
    @PrimaryKey
    val ticker: String,
    val value: Double,
)