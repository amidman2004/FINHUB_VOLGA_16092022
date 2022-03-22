package com.example.finnub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.finnub.ui.theme.FinnubTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finnub.ui.navGraphs.MainNavGraphs.SPLASH_SCREEN
import com.example.finnub.ui.navGraphs.MainNavGraphs.STOCKS_LIST
import com.example.finnub.ui.screens.splash_screen.SplashScreen
import com.example.finnub.ui.screens.stocks_list_screen.StocksListScreen
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinnubTheme {

                val mainNav = rememberNavController()
                val stocksListViewModel:StocksListViewModel = hiltViewModel()
                NavHost(navController = mainNav, startDestination = SPLASH_SCREEN){
                    composable(SPLASH_SCREEN){
                        SplashScreen(stocksListViewModel,mainNav)
                    }
                    composable(STOCKS_LIST){
                        StocksListScreen(mainNav = mainNav,stocksListViewModel)
                    }
                }


            }
        }
    }
}


