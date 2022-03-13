package com.example.finnub.data.api.models


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("p")
    val p: Double,
    @SerializedName("s")
    val s: String,
    @SerializedName("t")
    val t: Long,
    @SerializedName("v")
    val v: Double
)