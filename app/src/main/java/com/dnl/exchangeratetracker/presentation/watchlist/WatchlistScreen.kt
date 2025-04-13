package com.dnl.exchangeratetracker.presentation.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnl.exchangeratetracker.R
import com.dnl.exchangeratetracker.domain.Rate
import com.dnl.exchangeratetracker.presentation.components.CurrencyItem
import com.dnl.exchangeratetracker.presentation.components.EmptyView

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val rates: List<Rate>? by viewModel.favorites.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            rates.isNullOrEmpty() -> EmptyView(R.string.error_empty_response)
            errorMessage != null -> errorMessage?.let { EmptyView(it) }
            else -> rates?.let {
                WatchList(
                    rates = it,
                    onClick = { ticker -> viewModel.removeFavorite(ticker) }
                )
            }
        }
    }
}

@Composable
fun WatchList(
    rates: List<Rate>,
    onClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(rates, key = { item -> item.ticker }) { rate ->
            CurrencyItem(
                rate = rate
            ) {
                IconButton(onClick = { onClick(rate.ticker) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.action_delete_from_watchlist)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WatchlistScreenPreview() {
    val rates = listOf(
        Rate("USD", "1.0"),
        Rate("UAH", "10.0"),
    )
    WatchList(rates) { }
}