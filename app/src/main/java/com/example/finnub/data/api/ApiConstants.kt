package com.example.finnub.data.api

object ApiConstants {

    const val TOKEN = "sandbox_c8krd0aad3ibbdm42mag"//"c8krd0aad3ibbdm42ma0"

    const val BASE_URL = "https://finnhub.io/api/v1/"

    const val STOCKS_LIST_URL = "stock/symbol"
    const val STOCK_PRICE_URL = "quote"
    const val SEARCH_REQUEST = "search"
    const val STOCK_CANDLE = "stock/candle"

    const val WEB_SOCKET_URL = "wss://ws.finnhub.io?token=$TOKEN"


}