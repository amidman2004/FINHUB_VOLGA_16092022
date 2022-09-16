package com.example.finnub

import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.datastore.toJson
import com.example.finnub.data.datastore.toSimpleStockList
import org.junit.Assert
import org.junit.Test


class GsonTest {


    @Test
    fun `test serialize deserialize simpleStockList`(){
        val testData = mutableListOf<SimpleStock>(
            SimpleStock(
                "AA",
                0.0
            ),
            SimpleStock(
                "AA",
                10.0000
            )
        )
        val jsonData = testData.toJson()

        Assert.assertEquals(testData,jsonData.toSimpleStockList())
    }


}