package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ListOfSavedStock(
    vm:StocksListViewModel
) {

    val lifecycleOwner  = LocalLifecycleOwner.current

    var stockList:List<SimpleStock> by remember {
        mutableStateOf(listOf())
    }

    vm.savedStocks.observe(lifecycleOwner){
        stockList = it
    }

    if (stockList.isNotEmpty()){
        Text(text = "Избранное")
        LazyColumn(
            modifier = Modifier.height(220.dp)
        ){
            itemsIndexed(stockList){index,item ->
                StockListItem(
                    simpleStock = item,
                    vm = vm,
                    index = index,
                    stockList = vm.pageStocksList
                )
            }
        }
    }

    
}