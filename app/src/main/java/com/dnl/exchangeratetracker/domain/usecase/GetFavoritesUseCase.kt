package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.Repository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: Repository
) {
    fun execute() = repository.getFavorites()
}