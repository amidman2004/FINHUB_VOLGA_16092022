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
import com.example.finnub.domain.extensionmethods.toSimpleStockList
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


@Composable
fun StocksList(vm: StocksListViewModel) {


    var stockList:List<SimpleStock> by remember {
        mutableStateOf(listOf())
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    vm.pageStocksList.observe(lifecycleOwner){
        stockList = it
    }

    val state = rememberLazyListState()

    val loadingState by vm.stocksListLoadingState.collectAsState()

    val refreshState = rememberSwipeRefreshState(isRefreshing = loadingState == LoadingState.LoadingInProcess)

    Box(Modifier
        .fillMaxSize()
        ) {
            SwipeRefresh(
                state = refreshState,
                refreshTriggerDistance = 160.dp,
                onRefresh = {vm.Refresh()},
                indicator = { state, refreshDist ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshDist,
                        contentColor = finnhubGreen)
                }
            ) {
                LazyColumn(state = state, modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                ){
                    if (loadingState == LoadingState.LoadingInProcess)
                        item {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(700.dp)) {
                                CircularProgressIndicator(
                                    color = finnhubGreen,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    else
                        itemsIndexed(stockList){ index: Int, simpleStock: SimpleStock ->
                            StockListItem(
                                simpleStock = simpleStock,
                                vm = vm,
                                index = index)
                        }
                }
            }


        PageSwitcher(
            state = state,
            vm = vm,
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.BottomCenter)
        )

    }
}



suspend fun ScrollToStart(state: LazyListState){
    state.animateScrollToItem(0)
}

@Composable
fun StockListItem(
    simpleStock: SimpleStock,
    vm: StocksListViewModel,
    index:Int
    ) {

    var stockPrice by remember {
        mutableStateOf(simpleStock.price)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    vm.pageStocksList.observe(lifecycleOwner){
        val indexPrice = it[index].price
        if (indexPrice != 0.00)
        stockPrice = it[index].price
    }


    LaunchedEffect(key1 = simpleStock.symbol, block = {
        stockPrice = 0.00
        while (stockPrice == 0.00){
            val sPrice = vm.getStockPrice(symbol = simpleStock.symbol)
            stockPrice = sPrice
            delay(11000)
        }
    })

    Box(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(10))
            .border(1.dp, finnhubDarkBlue, RoundedCornerShape(10))) {
        Text(text = simpleStock.symbol,
            modifier = Modifier
                .padding(start = 30.dp)
                .align(Alignment.CenterStart))
        if (stockPrice == 0.00)
            CircularProgressIndicator(
                color = finnhubDarkBlue,
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
            )
        else
            Text(text = "${stockPrice.toBigDecimal()} $",
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd)
            )
    }

}
