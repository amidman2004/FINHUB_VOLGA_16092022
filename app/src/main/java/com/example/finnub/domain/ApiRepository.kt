package com.example.finnub.domain

import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.http.Query

interface ApiRepository {

    suspend fun getStockList(
        exchange:String,
        mic:String = "",
        securityType:String = "",
        currency:String = "",
    ):Flow<Resourse<List<StockSymbol>>>

    suspend fun pollingGetStockPrice(
        symbol: String
    ):Flow<Resourse<StockPrice>>

    fun openWebSocket(stockList: MutableStateFlow<List<SimpleStock>>)

    fun closeWebSocket()
}