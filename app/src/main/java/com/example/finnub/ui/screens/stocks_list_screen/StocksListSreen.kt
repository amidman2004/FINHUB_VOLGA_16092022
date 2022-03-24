package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.utils.extensionmethods.toSimpleStockList
import com.example.finnub.ui.screens.stocks_list_screen.ui_components.StocksList
import com.example.finnub.ui.theme.finnhubDarkBlue
import com.example.finnub.ui.theme.finnhubGreen
import com.example.finnub.utils.LoadingState
import com.example.finnub.utils.collectFlows
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StocksListScreen(
    mainNav:NavController,
    vm: StocksListViewModel
) {

    val loadingState by vm.stocksListLoadingState.collectAsState()

    Scaffold(topBar = {
        Column() {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = finnhubGreen,
            ) {
                Text(
                    text = "US STOCKS",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .align(CenterVertically))
            }
            ErrorLabel(error = loadingState.error,loadingState is LoadingState.LoadingError)
        }
    }) {

        StocksList(vm = vm)

    }
}




