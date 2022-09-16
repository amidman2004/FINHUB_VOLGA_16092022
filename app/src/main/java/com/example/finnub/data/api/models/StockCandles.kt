package com.example.finnub.data.api.models

data class StockCandles(
    val c: List<Double>,
    val h: List<Double>,
    val l: List<Double>,
    val o: List<Double>,
    val s: String,
    val t: List<Int>,
    val v: List<Int>
)