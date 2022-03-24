package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finnub.ui.theme.finnhubGreen
import kotlinx.coroutines.launch

@Composable
fun PageSwitcher(
    state: LazyListState,
    vm: StocksListViewModel,
    modifier: Modifier
) {
    val scope = rememberCoroutineScope()

    val currentPage by remember {
        vm.currentPage
    }

    Box(modifier = modifier){
        Row(
            modifier = Modifier
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                vm.prevPage()
                scope.launch {
                    ScrollToStart(state = state)
                }
            },colors = ButtonDefaults.buttonColors(
                backgroundColor = finnhubGreen
            )) {
                Text(text = "Prev Page")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = currentPage.toString())
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                vm.nextPage()
                scope.launch {
                    ScrollToStart(state = state)
                }
            },colors = ButtonDefaults.buttonColors(
                backgroundColor = finnhubGreen
            )) {
                Text(text = "Next Page")
            }
        }
    }
}

suspend fun ScrollToStart(state: LazyListState){
    state.animateScrollToItem(0)
}