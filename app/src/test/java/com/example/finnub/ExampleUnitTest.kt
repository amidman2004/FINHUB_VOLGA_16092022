package com.example.finnub

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import java.time.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun KotlinTest(){
        CoroutineScope(Dispatchers.IO).launch{
            assertEquals(true, isMarketOpen())
        }
    }
}

suspend fun isMarketOpen():Boolean
= CoroutineScope(Dispatchers.IO).async{
    val time = LocalDateTime.now(ZoneId.of("America/New_York")).plusMinutes(7)
    val day = time.dayOfWeek
    val hour = time.hour
    val minute:Double = time.minute.toDouble()/60
    val currentTime:Double = hour+minute
    if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY)
        return@async false
    if (currentTime >= 6.5 && currentTime<16.0)
        return@async true

    return@async false
}.await()