package com.example.finnub.domain.extensionmethods

import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockData
import com.google.gson.Gson
import kotlinx.coroutines.*

fun String.toSimpleStockList(emitList: List<SimpleStock>): List<SimpleStock> {

    val simpleStockList = Gson().fromJson(this@toSimpleStockList, StockData::class.java)
    val stockDataList = simpleStockList.data.distinctBy { data->
        data.s
    }

        stockDataList.forEach { data ->
             emitList.first { simpleStock: SimpleStock ->
                 simpleStock.symbol == data.s
             }
                 .price = data.p
        }

    return emitList
}