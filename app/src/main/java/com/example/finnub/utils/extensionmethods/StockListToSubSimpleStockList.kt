package com.example.finnub.utils.extensionmethods

import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockSymbol

fun List<SimpleStock>.toSubSimpleStockList(
    currentPage:Int,
    pageSize:Int
):List<SimpleStock>{
    val stockSymbolList = this
    try {
        return stockSymbolList
            .subList((currentPage-1)*pageSize,pageSize*currentPage)
    }catch (e:Exception){
        return emptyList()
    }

}