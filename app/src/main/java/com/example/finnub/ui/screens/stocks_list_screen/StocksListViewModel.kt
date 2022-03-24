package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.domain.ApiRepository
import com.example.finnub.utils.extensionmethods.savePage
import com.example.finnub.utils.extensionmethods.toSubSimpleStockList
import com.example.finnub.utils.LoadingState
import com.example.finnub.utils.LoadingState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.finnub.utils.Resourse.*
import kotlinx.coroutines.flow.*

@HiltViewModel
class StocksListViewModel @Inject
    constructor(
        private val apiRep:ApiRepository
    ):ViewModel() {

    private val _stocksList: MutableStateFlow<List<SimpleStock>> = MutableStateFlow(listOf())
    val stockList: StateFlow<List<SimpleStock>> = _stocksList.asStateFlow()

    private val _stocksListLoadingState:
            MutableStateFlow<LoadingState>
    = MutableStateFlow(LoadingStart)
    val stocksListLoadingState = _stocksListLoadingState.asStateFlow()

    private val pageSize = 8

    private val _currentPage = mutableStateOf(1)
    val currentPage: State<Int> = _currentPage

    private val _pageStocksList:MutableLiveData<List<SimpleStock>> = MutableLiveData(listOf())
    val pageStocksList:LiveData<List<SimpleStock>> = _pageStocksList



    init {
        viewModelScope.launch {
            _stocksListLoadingState.collect{ state->
                when(state){
                    is LoadingStart -> {
                        _stocksList.emit(listOf())
                        downloadStocksList()
                    }
                    is LoadingSuccess -> {
                        onPageMoved()
                    }

                    else -> {}
                }
            }
        }
    }
    fun nextPage(){
        if (_stocksList.value.size < _currentPage.value*_currentPage.value)
            return
        savePage()
        _currentPage.value++
        onPageMoved()
    }

    fun prevPage(){
        if (_currentPage.value == 1)
            return
        savePage()
        _currentPage.value--
        onPageMoved()
    }

    fun Refresh(){
        viewModelScope.launch {
            _stocksListLoadingState.emit(LoadingStart)
        }

    }

    private fun savePage(){
        viewModelScope.launch {
            _stocksList.savePage(
                page = _pageStocksList.value?: listOf(),
                currentPage = currentPage.value
            )
        }
    }

    private fun onPageMoved(){
        viewModelScope.launch {
            var pageList = _stocksList.value.toSubSimpleStockList(
                currentPage = _currentPage.value,
                pageSize = pageSize
            )
//            Most trend stocks for test WebSocket, uncomment code for test
//            pageList = listOf(
//                SimpleStock("AAPL"),
//                SimpleStock("AMZN"),
//                SimpleStock("MSFT"),
//                SimpleStock("BINANCE:BTCUSDT"))
            _pageStocksList.postValue(pageList)
            apiRep.closeWebSocket()
            apiRep.openWebSocket(_pageStocksList)
        }
    }



    private fun downloadStocksList() {
        viewModelScope.launch {
            apiRep.getStockList("US").collect { resource ->
                when (resource) {
                    is Loading -> {
                        _stocksListLoadingState.emit(LoadingInProcess)
                    }
                    is Error -> {
                        resource.error?.let{ error ->
                            _stocksListLoadingState.emit(LoadingError(error = error))
                        }
                    }
                    is Success -> {
                        resource.response?.let {
                            _stocksList.emit(it)
                        }
                        _stocksListLoadingState.emit(LoadingSuccess)
                    }
                }
            }
        }
    }

    suspend fun getStockPrice(symbol: String): Double{
        val response = apiRep.getStockPrice(symbol)
        _pageStocksList.value?.find {simpleStock -> simpleStock.symbol == symbol  }?.price = response
        return response
    }
}
