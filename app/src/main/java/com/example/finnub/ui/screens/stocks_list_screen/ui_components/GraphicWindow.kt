package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.core.entry.entryModelOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.finnub.ui.theme.finnhubDarkBlue
import com.example.finnub.ui.theme.finnhubGreen
import com.example.finnub.utils.LoadingState
import com.example.finnub.utils.collectFlow
import com.patrykandpatryk.vico.compose.axis.axisLabelComponent
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.horizontal.topAxis
import com.patrykandpatryk.vico.core.component.text.TextComponent
import com.patrykandpatryk.vico.core.legend.Legend
import com.patrykandpatryk.vico.core.marker.Marker
import kotlinx.coroutines.flow.onEach





@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun GraphicWindowDialog(
    isEnabled:Boolean,
    simpleStock: SimpleStock? = SimpleStock("AAPL",0.0),
    vm:StocksListViewModel,
    onDismiss:() -> Unit,
) {

    val listOfResolutions = listOf<String>("1", "5", "15", "30", "60", "D", "W", "M")

    val listOfTime = hashMapOf<String,Int>(
        "Year" to 29030400,

        "Week" to 604800,
        "Day" to 86400,
        "Month" to 2419200,

        )

    var currentResolution by remember {
        mutableStateOf("M")
    }

    var currentTime by remember {
        mutableStateOf(604800)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    vm.stocksListLoadingState.onEach {
        isLoading = it is LoadingState.LoadingInProcess
    }.collectFlow()
    
    if (isEnabled){

        val chart = columnChart()
        var model by remember {
            mutableStateOf(entryModelOf(*listOf<Double>().toTypedArray()))
        }

        var isEmpty by remember {
            mutableStateOf(false)
        }


        vm.currentCandle.onEach {
            isEmpty = it.isEmpty() || it.all { it == 0.0 }
            model = entryModelOf(*it.toTypedArray())
        }.collectFlow()



        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            buttons = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = {
                        if (simpleStock != null) {
                            vm.saveStock(simpleStock)
                        }
                    }) {
                        Text(text = "Добавить в избранное")
                    }
                    if (!isLoading){
                        if (isEmpty){
                            Box(modifier = Modifier.size(400.dp, 300.dp)){
                                Text(text = "График отсутсвует, повторите позже",modifier =Modifier.align(Alignment.Center))
                            }
                        }else
                        Chart(
                            modifier = Modifier.size(
                                400.dp,
                                300.dp,
                            ),
                            chart = chart,
                            model = model,
                            isHorizontalScrollEnabled = true,
                            bottomAxis = bottomAxis(label = axisLabelComponent()),

                        )
                    }else{
                        Box(modifier = Modifier.size(400.dp,300.dp)){
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier =
                        Modifier
                            .align(Alignment.End)
                            .padding(end = 30.dp)
                    ) {
                        Text(text = "Выйти")
                    }
                }


                LazyRow(Modifier.fillMaxWidth()) {
                    item {
                        Text(text = "Resolution:")
                    }
                    items(listOfResolutions) {
                        val selected = currentResolution == it
                        val color = if (selected) finnhubGreen else finnhubDarkBlue
                        TextButton(onClick = { currentResolution = it}) {
                            Text(text = it, color = color)
                        }
                    }
                }

                LazyRow(Modifier.fillMaxWidth()){
                    item { Text(text = "Period") }
                    items(listOfTime.keys.toList()) {
                        val selected = currentTime == listOfTime[it]
                        val color = if (selected) finnhubGreen else finnhubDarkBlue
                        TextButton(onClick = { currentTime = listOfTime[it] ?: 2419200}) {
                            Text(text = it, color = color)
                        }
                    }
                }
                Button(onClick = {vm.getStockCandle(currentResolution,currentTime)}) {
                    Text(text = "GetGraphic")
                }

            }
        )



        
    }

    
    
    
    

}