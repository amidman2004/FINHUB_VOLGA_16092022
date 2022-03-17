package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.domain.extensionmethods.toSimpleStockList
import com.example.finnub.utils.LoadingState
import com.example.finnub.utils.collectFlows
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun StocksListScreen(
    mainNav:NavController,
    vm: StocksListViewModel = hiltViewModel()) {

    val loadingState by vm.stocksListLoadingState.collectAsState()

    Scaffold(topBar = {
            ErrorLabel(error = loadingState.error,loadingState is LoadingState.LoadingError)
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
    val lifecycle = lifecycleOwner.lifecycle
    val lifecycleScope = lifecycleOwner.lifecycleScope

    collectFlows(lifecycle = lifecycle, lifecycleScope = lifecycleScope){
        vm.pageStocksList.onEach {
            stockList = it
        }.collect()

    }


    val page by remember {
        vm.currentPage
    }




    val state = rememberLazyListState()

    val loadingState by vm.stocksListLoadingState.collectAsState()


    Box(Modifier.fillMaxSize()) {
        LazyColumn(state = state, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
        ){
            if (loadingState == LoadingState.LoadingInProcess)
                item{
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(700.dp)){
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Blue
                        )
                    }
                }
            else
                itemsIndexed(stockList){ index: Int, simpleStock: SimpleStock ->
                    StockListItem(simpleStock,vm,page)

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
    page:Int
    ) {
    var stockPrice by remember(simpleStock) {
        mutableStateOf(simpleStock.price)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = page, block = {
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
            .height(100.dp)) {
        Text(text = simpleStock.symbol,
            modifier = Modifier
                .padding(start = 30.dp)
                .align(Alignment.CenterStart))
        if (stockPrice == 0.00)
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
            )
        else
            Text(text = stockPrice.toString(),
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd)
            )
    }

}
