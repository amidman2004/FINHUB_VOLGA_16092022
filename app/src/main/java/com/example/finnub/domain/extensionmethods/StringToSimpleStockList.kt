package com.example.finnub.domain.extensionmethods

import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockData
import com.google.gson.Gson
import kotlinx.coroutines.*

suspend fun String.toSimpleStockList(emitList: List<SimpleStock>): Deferred<List<SimpleStock>>
        = CoroutineScope(Dispatchers.Default).async {

    val simpleStockList = Gson().fromJson(this@toSimpleStockList, StockData::class.java)
    val stockDataList = simpleStockList.data.distinctBy { data->
        data.s
    }

    async {
        stockDataList.forEach { data ->

            launch {
                    emitList.first { simpleStock: SimpleStock ->
                        simpleStock.symbol == data.s
                    }
                        .price = data.p
            }
        }
    }.await()

    return@async emitList
}