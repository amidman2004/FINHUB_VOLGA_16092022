package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import kotlinx.coroutines.flow.onEach
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finnub.ui.theme.finnhubGreen
import com.example.finnub.ui.utils.BaseTextField
import com.example.finnub.utils.collectFlow
import com.example.finnub.R

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun SearchTab(
    vm:StocksListViewModel,
    modifier: Modifier = Modifier
) {

    val cancelPainter = painterResource(id = R.drawable.ic_baseline_cancel_24)
    val searchPainter = painterResource(id = R.drawable.ic_baseline_search_24)

    var searchValue by remember {
        mutableStateOf("")
    }

    vm.searchValue.onEach {
        searchValue = it
    }.collectFlow()

    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ){
        BaseTextField(
            value = searchValue,
            onValueChanged = {
                vm.changeSearchValue(it)
            },
            modifier = Modifier
                .size(
                    width = 300.dp,
                    height = 55.dp
                )
                .padding(start = 15.dp)
        
        )
        IconButton(onClick = { vm.searchStocks() }) {
            Icon(
                painter = searchPainter,
                contentDescription = null,
                tint = finnhubGreen,
                modifier = Modifier.size(
                    50.dp
                )
            )
        }
        if (searchValue.isNotEmpty())
        IconButton(onClick = {
            vm.changeSearchValue("")
            vm.refresh()
        }) {
            Icon(
                painter = cancelPainter,
                contentDescription = null,
                tint = finnhubGreen,
                modifier = Modifier.size(
                    50.dp
                )
            )
        }

    }
}