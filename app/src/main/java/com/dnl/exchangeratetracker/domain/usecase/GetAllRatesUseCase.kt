package com.dnl.exchangeratetracker.domain.usecase

import com.dnl.exchangeratetracker.data.Repository
import com.dnl.exchangeratetracker.domain.ConnectionException
import com.dnl.exchangeratetracker.domain.Rate
import com.dnl.exchangeratetracker.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllRatesUseCase @Inject constructor(
    private val repository: Repository
) {
    fun execute(): Flow<Resource<List<Rate>>> {
        return repository.getAllRates()
            .map { result ->
                if (result.isEmpty()) Resource.ErrorType.EmptyResponse
                else Resource.Success(result)
            }
            .catch { exception ->
                when(exception) {
                    is ConnectionException -> Resource.ErrorType.Network
                    else -> Resource.ErrorType.Unknown
                }
            }
    }
}