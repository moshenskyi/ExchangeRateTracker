package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.Repository
import javax.inject.Inject

class RemoveFavoriteItemUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(ticker: String) {
        repository.removeFavorite(ticker)
    }
}