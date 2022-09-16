package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import com.example.finnub.ui.theme.finnhubGreen

@Composable
fun FinnhubTopAppBar(
    vm:StocksListViewModel,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = finnhubGreen,
    ) {
        ExchangeSelector(vm = vm)
    }
}