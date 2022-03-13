package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.utils.collectFlows
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun StocksListScreen(
    mainNav:NavController,
    vm: StocksListViewModel = hiltViewModel()) {

    Scaffold(topBar = {

    }) {
        StocksList(vm = vm)
    }
}

@Composable
fun StocksList(vm: StocksListViewModel) {

    val stockList by vm.stockList.collectAsState()

    val state = rememberLazyListState()



    val list = state.layoutInfo.visibleItemsInfo

    LazyColumn(state = state, modifier = Modifier.fillMaxSize()){
        itemsIndexed(stockList){index: Int, stockSymbol: StockSymbol ->
            StockListItem(
                stockSymbol = stockSymbol,
                vm = vm,
                index = index,
                list = list
                )
        }
    }

}

@Composable
fun StockListItem(
    stockSymbol: StockSymbol,
    vm: StocksListViewModel,
    index:Int,
    list:List<LazyListItemInfo>
    ) {


    var stockPrice:StockPrice? by remember {
        mutableStateOf(null)
    }


    Box(
        Modifier
            .fillMaxWidth()
            .height(100.dp)) {
        Text(text = stockSymbol.symbol,
            modifier = Modifier
                .padding(start = 30.dp)
                .align(Alignment.CenterStart))
        Text(text =
        stockPrice?.let { stock->
            stock.c.toString()
        }?: "null",
            modifier = Modifier
                .padding(end = 30.dp)
                .align(Alignment.CenterEnd))
    }
}
