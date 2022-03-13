package com.example.finnub.data.api

import com.example.finnub.data.api.ApiURLs.WEB_SOCKET_URL
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.domain.ApiRepository
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Response
import javax.inject.Inject

class ApiRepositoryImpl @Inject
    constructor(
    private val api:ApiRequests
):ApiRepository {

    var ws:WebSocket? = null

    override suspend fun getStockList(
        exchange: String,
        mic: String,
        securityType: String,
        currency: String
    ): Flow<Resourse<List<StockSymbol>>>
    = flow {
        try {
            val connect = api.getStocksList(exchange, mic, securityType, currency)
            if (connect.isSuccessful){
                connect.body()?.let { stocksList ->
                    emit(Resourse.Success(response = stocksList))
                }
            }else{
                emit(Resourse.Error(connect.code().toString()))
            }
        }catch (e:Exception){
            emit(Resourse.Error(e.toString()))
        }
    }

    override suspend fun pollingGetStockPrice(symbol: String): Flow<Resourse<StockPrice>>
    = flow {
        emit(Resourse.Loading())
        this.getStockPrice(symbol)
        while (true){
            delay(30000)
            this.getStockPrice(symbol)
        }
    }

    private suspend fun FlowCollector<Resourse<StockPrice>>.getStockPrice(symbol: String,){
        try {
            emit(Resourse.Loading())
            val connect = api.getStockPrice(symbol = symbol)
            if (connect.isSuccessful){
                connect.body()?.let { stockPrice: StockPrice ->
                    emit(Resourse.Success(response = stockPrice))
                }
            }else{
                emit(Resourse.Error(connect.code().toString()))
            }
        }catch (e:Exception){
            emit(Resourse.Error(e.toString()))
        }
    }

    override fun openWebSocket(stockList:MutableStateFlow<List<SimpleStock>>){
        val request = Request.Builder()
            .url(WEB_SOCKET_URL)
            .build()
        val listener = AmidWebSocketListener(stockList = stockList)

        ws = OkHttpClient().newWebSocket(request,listener)
    }

    override fun closeWebSocket(){
        CoroutineScope(Dispatchers.IO).launch{
            ws?.close(100,null)
        }
    }

}