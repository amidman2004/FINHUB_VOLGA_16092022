package com.example.finnub.domain.extensionmethods

import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockSymbol

fun List<StockSymbol>.toSubSimpleStockList(
    currentPage:Int,
    pageSize:Int
):List<SimpleStock>{
    val stockSymbolList = this
    return stockSymbolList
        .subList((currentPage-1)*pageSize,pageSize*currentPage)
        .map { stockSymbol ->
            SimpleStock(stockSymbol.symbol)
        }
}