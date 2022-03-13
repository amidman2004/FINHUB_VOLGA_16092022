package com.example.finnub.ui.screens.stocks_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.data.api.models.StockPrice
import com.example.finnub.data.api.models.StockSymbol
import com.example.finnub.domain.ApiRepository
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

    private val _stocksList: MutableStateFlow<List<StockSymbol>> = MutableStateFlow(listOf())
    val stockList: StateFlow<List<StockSymbol>> = _stocksList.asStateFlow()

    private val _stocksListLoadingState:
            MutableStateFlow<LoadingState>
    = MutableStateFlow(LoadingStart)
    val stocksListLoadingState = _stocksListLoadingState

    private var currentPage = 1

    private val _pageStocksList:MutableStateFlow<List<SimpleStock>> = MutableStateFlow(listOf())
    val pageStocksList:SharedFlow<List<SimpleStock>> = _pageStocksList

    init {
        viewModelScope.launch {
            _stocksListLoadingState.collect{ state->
                when(state){
                    is LoadingStart -> {
                        _stocksList.emit(listOf())
                        downloadStocksList()
                    }
                    is LoadingInProcess -> {}
                    is LoadingNothing -> {}
                    is LoadingSuccess -> {}
                    is LoadingError -> {}
                }
            }
        }
    }
    fun nextPage(){
        viewModelScope.launch {
            currentPage++
            val pageList = _stocksList.value
                .subList(0*currentPage,20*currentPage)
                .map { stockSymbol ->
                    SimpleStock(stockSymbol.symbol)
                }
            _pageStocksList.emit(pageList)
        }
    }
    fun prevPage(){
        viewModelScope.launch {
            if (currentPage == 1)
                return@launch
            currentPage++
            val pageList = _stocksList.value
                .subList(0*currentPage,20*currentPage)
                .map { stockSymbol ->
                    SimpleStock(stockSymbol.symbol)
                }
            _pageStocksList.emit(pageList)
        }
    }



    private fun downloadStocksList() {
        viewModelScope.launch {
            apiRep.getStockList("US").collect { resource ->
                when (resource) {
                    is Loading -> {

                    }
                    is Error -> {
                        _stocksListLoadingState.emit(LoadingError)
                    }
                    is Success -> {
                        resource.response?.let {
                            _stocksList.emit(it)
                        }
                    }
                }
            }
        }
    }

    suspend fun getStockPrice(symbol: String): StateFlow<StockPrice?> = flow {
        apiRep.pollingGetStockPrice(symbol).collect { resource ->
            when (resource) {
                is Loading -> {

                }
                is Error -> {

                }
                is Success -> {
                    emit(resource.response)
                }
            }
        }
    }.stateIn(viewModelScope,)
}
