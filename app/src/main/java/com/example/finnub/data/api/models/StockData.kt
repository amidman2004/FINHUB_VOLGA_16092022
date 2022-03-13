package com.example.finnub.data.api.models


import com.google.gson.annotations.SerializedName

data class StockData(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("type")
    val type: String
)