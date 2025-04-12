package com.dnl.exchangeratetracker.presentation.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnl.exchangeratetracker.R
import com.dnl.exchangeratetracker.domain.Rate
import com.dnl.exchangeratetracker.presentation.components.CurrencyItem
import com.dnl.exchangeratetracker.presentation.components.EmptyView

@Composable
fun AddCurrenciesScreen(
    viewModel: CurrencySelectionViewModel = hiltViewModel()
) {
    val rates: List<Rate>? by viewModel.filteredCurrencies.collectAsStateWithLifecycle()

    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.updateQuery(it) },
            onClearClick = { viewModel.clearSearch() },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when {
            errorMessage != null -> errorMessage?.let { EmptyView(it) }
            else -> rates?.let { CurrencyList(viewModel, it) }
        }
    }
}

@Composable
private fun CurrencyList(
    viewModel: CurrencySelectionViewModel,
    rates: List<Rate>
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(rates) { rate ->
            CurrencyItem(
                rate = rate,
                onClick = { viewModel.toggleFavorite(rate) }
            ) {
                CircularCheckbox(
                    checked = favorites?.firstOrNull { it.ticker == rate.ticker } != null,
                    onCheckedChange = { viewModel.toggleFavorite(rate) }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { Text(stringResource(R.string.searchbar_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.action_search)
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.action_clear_search)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun CircularCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val borderColor = if (checked) primaryColor else Color.Gray.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .size(24.dp)
            .border(2.dp, borderColor, CircleShape)
            .clip(CircleShape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(primaryColor)
            )
        }
    }
}