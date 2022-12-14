package com.example.finnub.data.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockData
import com.example.finnub.utils.extensionmethods.toSimpleStockList
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
class AmidWebSocketListener(
    private val stockList:MutableLiveData<List<SimpleStock>>,
    private val savedStockList:MutableLiveData<List<SimpleStock>>
):WebSocketListener() {

    private suspend fun sendMessage(webSocket: WebSocket,symbol:String){
        val message = "{\"type\":\"subscribe\",\"symbol\":\"$symbol\"}"
        webSocket.send(message)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        CoroutineScope(Dispatchers.IO).launch{
                stockList.value?.forEach {simpleStock->
                    launch {
                        sendMessage(webSocket = webSocket, symbol = simpleStock.symbol)
                    }
                }
                savedStockList.value?.forEach {simpleStock->
                    launch {
                        sendMessage(webSocket = webSocket, symbol = simpleStock.symbol)
                    }
                }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {

    }

    override fun onMessage(webSocket: WebSocket, text: String) {

        CoroutineScope(Dispatchers.IO).launch {
            if (text == "{\"type\":\"ping\"}")
                return@launch
            stockList.value?.let { list ->
                val emitList = text.toSimpleStockList(list)
                stockList.postValue(emitList)
            }
            savedStockList.value?.let { list ->
                val emitList = text.toSimpleStockList(list)
                stockList.postValue(emitList)
            }
        }

    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

    }


}