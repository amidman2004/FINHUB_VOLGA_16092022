package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import com.example.finnub.ui.theme.finnhubDarkBlue
import kotlinx.coroutines.delay


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
        stockPrice = simpleStock.price
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