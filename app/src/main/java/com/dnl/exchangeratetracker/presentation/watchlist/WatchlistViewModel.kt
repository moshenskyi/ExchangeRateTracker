package com.dnl.exchangeratetracker.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnl.exchangeratetracker.R
import com.dnl.exchangeratetracker.domain.Rate
import com.dnl.exchangeratetracker.domain.Resource
import com.dnl.exchangeratetracker.domain.Resource.ErrorType
import com.dnl.exchangeratetracker.domain.usecase.GetAllRatesUseCase
import com.dnl.exchangeratetracker.domain.usecase.GetFavoritesUseCase
import com.dnl.exchangeratetracker.domain.usecase.RemoveFavoriteItemUseCase
import com.dnl.exchangeratetracker.presentation.messageResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ANR_TIMEOUT = 5_000L
private const val REFRESH_DELAY = 5_000L

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val removeFavoriteItemUseCase: RemoveFavoriteItemUseCase,
    getFavoritesUseCase: GetFavoritesUseCase,
    getAllRatesUseCase: GetAllRatesUseCase
) : ViewModel() {
    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(R.string.error_empty_response)
    val errorMessage = _errorMessage.asStateFlow()

    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    private var autoRefreshJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val favorites: StateFlow<List<Rate>?> = _refreshTrigger
        .flatMapLatest {
            combine(
                getFavoritesUseCase.execute(),
                getAllRatesUseCase.execute()
            ) { favoriteRates, allRatesResult ->
                when (allRatesResult) {
                    is Resource.Success<List<Rate>> -> {
                        composeWatchlist(favoriteRates, allRatesResult)
                    }

                    is ErrorType -> {
                        favoriteRates.ifEmpty {
                            _errorMessage.value = allRatesResult.messageResource()
                            emptyList()
                        }
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ANR_TIMEOUT),
            initialValue = emptyList()
        )

    private fun composeWatchlist(
        favoriteRates: List<Rate>,
        allRatesResult: Resource.Success<List<Rate>>
    ): List<Rate> {
        val result = favoriteRates.map { favoriteRate ->
            allRatesResult.data.find { it.ticker == favoriteRate.ticker } ?: favoriteRate
        }
        if (result.isEmpty()) _errorMessage.value = R.string.error_empty_response
        else _errorMessage.value = null
        return result
    }

    init {
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(REFRESH_DELAY)
                refresh()
            }
        }
    }

    fun removeFavorite(ticker: String) {
        viewModelScope.launch { removeFavoriteItemUseCase.execute(ticker) }
    }

    private fun refresh() {
        _refreshTrigger.tryEmit(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }
}