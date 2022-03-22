package com.example.finnub

import androidx.datastore.preferences.protobuf.Api
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.finnub.data.api.ApiRepositoryImpl
import com.example.finnub.data.api.ApiRequests
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.domain.ApiRepository
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FinnhubTest{

    private fun isMarketOpen():Boolean{
        val time = LocalDateTime.now(ZoneId.of("America/New_York"))
        val day = time.dayOfWeek
        val hour = time.hour
        val minute:Double = time.minute.toDouble()/60
        val currentTime:Double = hour+minute
        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY)
            return false
        if (currentTime >= 6.5 && currentTime<16.0)
            return true

        return false
    }

    @Test
    fun marketOpenTest(){
        assertEquals(true,isMarketOpen())
    }

}


