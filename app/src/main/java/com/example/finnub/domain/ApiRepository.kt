package com.example.finnub.domain

import androidx.lifecycle.MutableLiveData
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockCandles
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import retrofit2.http.Query

interface ApiRepository {

    suspend fun getStockList(
        exchange:String,
        mic:String = "",
        securityType:String = "",
        currency:String = "",
    ):Flow<Resourse<List<SimpleStock>>>

    suspend fun getStockPrice(symbol: String):Double

    fun openWebSocket(stockList:MutableLiveData<List<SimpleStock>>,savedStockList:MutableLiveData<List<SimpleStock>>)

    fun closeWebSocket()

    fun getStockCandle(symbol: String,resolution:String,timeValue:Int):Flow<Resourse<StockCandles>>
}