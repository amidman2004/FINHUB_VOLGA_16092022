package com.example.finnub.ui.screens.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finnub.ui.theme.finnhubDarkBlue
import com.example.finnub.ui.theme.finnhubGreen
import com.example.finnub.R
import com.example.finnub.ui.navGraphs.MainNavGraphs.STOCKS_LIST
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import com.example.finnub.utils.LoadingState
import com.example.finnub.utils.collectFlows

@Composable
fun SplashScreen(vm: StocksListViewModel, mainNav:NavController) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit, block = {
        collectFlows(lifecycleOwner = lifecycleOwner){
            vm.stocksListLoadingState.collect{ loadingState->
                if (loadingState is LoadingState.LoadingSuccess
                    || loadingState is LoadingState.LoadingError)
                        mainNav.navigate(STOCKS_LIST)
            }
        }
    })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(finnhubGreen)){
        Column(
            modifier = Modifier
                .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.finnhub_logo),
                contentDescription = null,
                modifier = Modifier.size(146.dp,120.dp))
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Finnhub Stocks",
                color = finnhubDarkBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            CircularProgressIndicator(
                color = finnhubDarkBlue,
            )
        }

        Text(
            text = "Powered by IT`Volga",
            color = finnhubDarkBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp))
    }
}