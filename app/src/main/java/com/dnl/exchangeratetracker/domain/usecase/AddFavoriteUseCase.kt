package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.Repository
import com.dnl.exchangeratetracker.domain.Rate
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(rate: Rate) = repository.addFavorite(rate)
}