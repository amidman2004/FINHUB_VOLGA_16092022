package com.example.finnub

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.domain.ApiRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
@HiltAndroidTest
class FinnhubInstrumentedTest  {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var apiRep:ApiRepository

    @Before
    fun init(){
        hiltRule.inject()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stockPriceTest() = runBlocking{
        val price = apiRep.getStockPrice("AAPL")
        println(price)
    }

    @Test
    fun stockListTest() = runBlocking {
        apiRep.getStockList("US").onEach { resource->
            println(resource.error)
            println(resource.response)
        }.collect()
    }

    //launch with debug
    @Test
    fun webSocketPriceTest() = runBlocking {
        val testList:MutableLiveData<List<SimpleStock>>
        = MutableLiveData(listOf(
                SimpleStock("AAPL"),
                SimpleStock("AMZN"),
                SimpleStock("MSFT"),
                SimpleStock("BINANCE:BTCUSDT")
        ))
        apiRep.openWebSocket(testList)

        testList.asFlow().onEach {stockList->
            stockList.forEach { simpleStock->
                launch {
                    if (simpleStock.price != 0.00)
                        println(simpleStock)
                }
            }
        }.collect()

    }
}