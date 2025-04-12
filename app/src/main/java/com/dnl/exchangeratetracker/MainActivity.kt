package com.dnl.exchangeratetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dnl.exchangeratetracker.presentation.Screen
import com.dnl.exchangeratetracker.presentation.selection.AddCurrenciesScreen
import com.dnl.exchangeratetracker.presentation.watchlist.WatchlistScreen
import com.dnl.exchangeratetracker.ui.theme.ExchangeRateTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExchangeRateTrackerTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: Screen.WatchList.route

                val showFab = currentRoute != Screen.Currencies.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute == Screen.Currencies.route) {
                            AppBar(navController)
                        }
                    },
                    floatingActionButton = {
                        if (showFab) {
                            FloatingActionButton(onClick = {
                                navController.navigate(Screen.Currencies.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.action_add_to_watchlist)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.WatchList.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Currencies.route) {
                            AddCurrenciesScreen()
                        }
                        composable(Screen.WatchList.route) {
                            WatchlistScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun AppBar(navController: NavHostController) {
        TopAppBar(
            title = { Text(stringResource(R.string.add_to_watchlist_title)) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            R.string.action_back
                        )
                    )
                }
            }
        )
    }
}
