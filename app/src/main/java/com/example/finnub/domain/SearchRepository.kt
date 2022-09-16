package com.example.finnub.domain

import com.example.finnub.data.api.models.SearchResponse
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SearchRepository {

    suspend fun searchStocks(
        stockName:String
    ): Flow<Resourse<List<SimpleStock>>>
}