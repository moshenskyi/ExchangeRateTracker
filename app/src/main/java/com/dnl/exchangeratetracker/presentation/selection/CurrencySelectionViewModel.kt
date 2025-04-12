package com.dnl.exchangeratetracker.presentation.selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnl.exchangeratetracker.R
import com.dnl.exchangeratetracker.domain.Rate
import com.dnl.exchangeratetracker.domain.Resource
import com.dnl.exchangeratetracker.domain.Resource.ErrorType
import com.dnl.exchangeratetracker.domain.usecase.AddFavoriteUseCase
import com.dnl.exchangeratetracker.domain.usecase.GetAllRatesUseCase
import com.dnl.exchangeratetracker.domain.usecase.GetFavoritesUseCase
import com.dnl.exchangeratetracker.domain.usecase.RemoveFavoriteItemUseCase
import com.dnl.exchangeratetracker.presentation.messageResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val ANR_TIMEOUT = 5_000L
private const val SEARCH_UPDATE_DELAY = 500

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    private val removeFavoriteItemUseCase: RemoveFavoriteItemUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    getAllRatesUseCase: GetAllRatesUseCase,
    getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(R.string.error_empty_response)
    val errorMessage = _errorMessage.asStateFlow()

    private val rates: Flow<List<Rate>> = getAllRatesUseCase.execute()
        .map { resource ->
            when (resource) {
                is ErrorType -> {
                    _errorMessage.value = resource.messageResource()
                    emptyList()
                }
                is Resource.Success<List<Rate>> -> {
                    _errorMessage.value = null
                    resource.data
                }
            }
        }

    val favorites: StateFlow<List<Rate>?> = getFavoritesUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ANR_TIMEOUT),
            initialValue = null
        )

    @OptIn(FlowPreview::class)
    val filteredCurrencies =
        combine(rates, _searchQuery) { rates, query -> filterResults(query, rates) }
            .debounce(SEARCH_UPDATE_DELAY.toDuration(DurationUnit.MILLISECONDS))
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = emptyList()
            )

    private fun filterResults(
        query: String,
        rates: List<Rate>
    ): List<Rate> {
        return if (query.isEmpty()) {
            rates
        } else {
            rates.filter { rate ->
                rate.ticker.contains(query, ignoreCase = true)
            }
        }
    }

    fun toggleFavorite(rate: Rate) {
        viewModelScope.launch {
            val isFavorite = favorites.value?.firstOrNull { it.ticker == rate.ticker } != null
            if (isFavorite) {
                removeFavoriteItemUseCase.execute(rate.ticker)
            } else {
                addFavoriteUseCase.execute(rate)
            }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}