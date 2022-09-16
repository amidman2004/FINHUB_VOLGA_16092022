package com.example.finnub.domain

import com.example.finnub.data.api.models.SimpleStock

interface SaveStockRepository {

    suspend fun addStock(stock:SimpleStock)

    suspend fun getStockList():List<SimpleStock>

    suspend fun deleteFromStockList(stockName: String)

}