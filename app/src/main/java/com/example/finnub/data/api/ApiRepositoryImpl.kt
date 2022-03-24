package com.example.finnub.data.api

import androidx.lifecycle.MutableLiveData
import com.example.finnub.data.api.ApiConstants.WEB_SOCKET_URL
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.domain.ApiRepository
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
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
    ): Flow<Resourse<List<SimpleStock>>>
    = flow {
        emit(Resourse.Loading())
        try {
            val connect = api.getStocksList(exchange, mic, securityType, currency)
            if (connect.isSuccessful){
                connect.body()?.let { stocksList ->
                    val simpleStockList = stocksList.map { stockSymbol ->
                        SimpleStock(stockSymbol.symbol)
                    }
                    emit(Resourse.Success(response = simpleStockList))
                }
            }else{
                if (connect.code() == 429)
                    emit(Resourse.Error("\n Too Many Requests, please wait 10-30 seconds and restart"))
                else
                    emit(Resourse.Error("\n " +
                            "code ${connect.code()} ${connect.message()}"))
            }
        }catch (e:Exception){
            emit(Resourse.Error("\n Check your Internet connection and retry download"))
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



    override fun openWebSocket(stockList:MutableLiveData<List<SimpleStock>>){
        val request = Request.Builder()
            .url(WEB_SOCKET_URL)
            .build()
        val listener = AmidWebSocketListener(stockList = stockList)
        val client = OkHttpClient()
        ws = client.newWebSocket(request,listener)

    }

    override fun closeWebSocket(){
        ws?.let { webSocket ->  webSocket.cancel() }
    }

}