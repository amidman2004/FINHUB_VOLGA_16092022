package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finnub.utils.Constants
import com.example.finnub.utils.collectFlow
import kotlinx.coroutines.flow.onEach


@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ExchangeSelector(
    vm:StocksListViewModel
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    var currentExchangeValue by remember {
        mutableStateOf("US")
    }


    val scrollState = rememberScrollState()

    val exchangeList = Constants.EXCHANGES

    vm.currentExchange.onEach {
        currentExchangeValue = it
    }.collectFlow()

    Column {
        Text(
            text = "$currentExchangeValue STOCKS",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 40.dp)
                .clickable {
                    isExpanded = !isExpanded
                }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .padding(start = 40.dp)
                .width(70.dp)
                .height(170.dp)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            exchangeList.forEach {
                DropdownMenuItem(
                    modifier = Modifier.align(CenterHorizontally),
                    onClick = {
                        vm.changeCurrentExchange(it)
                    }
                ) {
                    Text(text = it)
                }
            }
        }
    }

}