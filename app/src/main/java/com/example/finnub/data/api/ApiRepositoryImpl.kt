package com.example.finnub.data.api

import com.example.finnub.data.api.ApiURLs.WEB_SOCKET_URL
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.domain.ApiRepository
import com.example.finnub.utils.LoadingState
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
import java.net.UnknownHostException
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
        emit(Resourse.Loading())
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
            if (e.toString().contains("UnknownHostException"))
                emit(Resourse.Error("No Internet"))
            else
                emit(Resourse.Error(e.toString()))
        }
    }


    override suspend fun getStockPrice(symbol: String):Double{
        try {
            val connect = api.getStockPrice(symbol = symbol)
            if (connect.isSuccessful)
                return connect.body()?.c?:0.00
            else
                return 0.00
        }catch (e:Exception){
            return 0.00
        }
    }



    override fun openWebSocket(stockList:MutableStateFlow<List<SimpleStock>>){
        val request = Request.Builder()
            .url(WEB_SOCKET_URL)
            .build()
        val listener = AmidWebSocketListener(stockList = stockList)
        val client = OkHttpClient()
        ws = client.newWebSocket(request,listener)

    }

    override fun closeWebSocket(){
        CoroutineScope(Dispatchers.IO).launch{
            ws?.let {webSocket ->  webSocket.close(1000,null) }
        }
    }

}