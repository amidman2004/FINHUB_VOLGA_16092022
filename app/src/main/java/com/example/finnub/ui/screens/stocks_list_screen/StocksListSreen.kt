package com.example.finnub.ui.screens.stocks_list_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.utils.LoadingState
import com.example.finnub.utils.collectFlows
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.*

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

    val stockList by vm.pageStocksList.collectAsState(listOf())

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
                itemsIndexed(stockList){index: Int, simpleStock: SimpleStock ->
                    StockListItem(simpleStock)
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
    simpleStock: SimpleStock
    ) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(100.dp)) {
        Text(text = simpleStock.symbol,
            modifier = Modifier
                .padding(start = 30.dp)
                .align(Alignment.CenterStart))
        if (simpleStock.price == 0.00)
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
            )
        else
            Text(text = simpleStock.price.toString(),
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd)
            )
    }

}
