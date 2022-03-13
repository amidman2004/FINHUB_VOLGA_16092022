package com.example.finnub.data.api

import com.example.finnub.data.api.ApiURLs.STOCKS_LIST_URL
import com.example.finnub.data.api.ApiURLs.STOCK_PRICE_URL
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequests {

    @GET(STOCKS_LIST_URL)
    suspend fun getStocksList(
        @Query("exchange") exchange:String,
        @Query("mic") mic:String = "",
        @Query("securityType") securityType:String = "",
        @Query("currency") currency:String = "",
    ):Response<List<StockSymbol>>

    @GET(STOCK_PRICE_URL)
    suspend fun getStockPrice(
        @Query("symbol") symbol:String
    ):Response<StockPrice>


}