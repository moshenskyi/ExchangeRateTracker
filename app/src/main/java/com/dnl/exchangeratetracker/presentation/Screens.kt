package com.dnl.exchangeratetracker.presentation

sealed class Screen(val route: String) {
    data object Currencies : Screen("currencies")
    data object WatchList : Screen("watchList")
}
