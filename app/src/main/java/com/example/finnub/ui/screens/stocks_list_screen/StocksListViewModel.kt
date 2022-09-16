package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.domain.ApiRepository
import com.example.finnub.domain.SaveStockRepository
import com.example.finnub.domain.SearchRepository
import com.example.finnub.utils.Constants
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
        private val apiRep:ApiRepository,
        private val searchRepository: SearchRepository,
        private val saveStockRepository: SaveStockRepository,
    ):ViewModel() {

    private val _stocksList: MutableStateFlow<List<SimpleStock>> = MutableStateFlow(listOf())
    val stockList: StateFlow<List<SimpleStock>> = _stocksList.asStateFlow()

    private val _currentExchange = MutableStateFlow("US")
    val currentExchange = _currentExchange.asStateFlow()

    private val _stocksListLoadingState:
            MutableStateFlow<LoadingState>
    = MutableStateFlow(LoadingStart)
    val stocksListLoadingState = _stocksListLoadingState.asStateFlow()

    private val pageSize = 8

    private val _currentPage = mutableStateOf(1)
    val currentPage: State<Int> = _currentPage

    private val _pageStocksList:MutableLiveData<List<SimpleStock>> = MutableLiveData(listOf())
    val pageStocksList:LiveData<List<SimpleStock>> = _pageStocksList

    private val _searchValue = MutableStateFlow("")
    val searchValue = _searchValue.asStateFlow()

    private val _isDialogEnabled = MutableStateFlow(false)
    val isDialogEnabled = _isDialogEnabled.asStateFlow()

    private val _currentStock = MutableStateFlow<SimpleStock?>(null)
    val currentStock = _currentStock.asStateFlow()

    private val _savedStocks = MutableLiveData<List<SimpleStock>>(listOf())
    val savedStocks:LiveData<List<SimpleStock>> = _savedStocks

    private val _currentCandle = MutableStateFlow(listOf<Double>())
    val currentCandle = _currentCandle.asStateFlow()


    init {
        viewModelScope.launch {
            _stocksListLoadingState.collect{ state->
                when(state){
                    is LoadingStart -> {
                        getStockList()
                        _stocksList.emit(listOf())
                        val searchValue = _searchValue.value.trim()
                        if (searchValue.isEmpty())
                            downloadStocksList()
                        else
                            searchStocks()
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
        val currentFullList = stockList.value.size
        val currentPage = currentPage.value + 1
        if (currentFullList < currentPage * pageSize)
            return
        else{
            savePage()
            _currentPage.value++
            onPageMoved()
        }

    }

    fun prevPage(){
        if (_currentPage.value == 1)
            return
        savePage()
        _currentPage.value--
        onPageMoved()
    }

    fun toFirstPage(){
        savePage()
        _currentPage.value = 1
        onPageMoved()
    }

    fun refresh(){
        viewModelScope.launch {
            _stocksListLoadingState.emit(LoadingStart)
        }

    }

    private fun savePage(){
        viewModelScope.launch {
            val currentPageListValue = pageStocksList.value
            _stocksList.savePage(
                page = currentPageListValue?: listOf(),
                currentPage = currentPage.value
            )
        }
    }

    private fun onPageMoved(){
        viewModelScope.launch {
            val currentValue = stockList.value
            var pageList = currentValue.toSubSimpleStockList(
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
            apiRep.openWebSocket(_pageStocksList,_savedStocks)
        }
    }



    private fun downloadStocksList() {
        viewModelScope.launch {
            val currentExchange = currentExchange.value
            apiRep.getStockList(currentExchange).collect { resource ->
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
                        toFirstPage()
                    }
                }
            }
        }
    }

    fun searchStocks(){

        val stockName = searchValue.value.trim()
        if (stockName.isEmpty())
            return
        viewModelScope.launch {
            searchRepository.searchStocks(stockName).collect{ resource ->
                when(resource){
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
                        toFirstPage()
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

    fun changeSearchValue(value: String) {
        viewModelScope.launch {
            _searchValue.emit(value)
        }
    }



    fun changeCurrentExchange(value: String){
        if (!Constants.EXCHANGES.contains(value))
            return
        viewModelScope.launch {
            _currentExchange.emit(value)
            downloadStocksList()
        }
    }

    fun setDialogValue(dialogValue:Boolean,currentSimpleStock: SimpleStock? = null){
        viewModelScope.launch {
            _isDialogEnabled.emit(dialogValue)
            _currentStock.emit(currentSimpleStock)
        }
    }

    fun saveStock(stock: SimpleStock){
        viewModelScope.launch {
            saveStockRepository.addStock(stock)
            getStockList()
        }
    }

    fun getStockCandle(
        resolution:String,
        timeValue:Int
    ){
        val symbol = currentStock.value?.symbol ?: "AAPL"
        viewModelScope.launch {
            apiRep.getStockCandle(
                symbol = symbol,
                resolution = resolution,
                timeValue
            ).collect{ resource ->
                when(resource){
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
                            if (it.s == "no_data")
                                _currentCandle.emit(listOf())
                            else
                                _currentCandle.emit(it.c)
                        }
                        _stocksListLoadingState.emit(LoadingSuccess)
                    }
                }
            }
        }
    }

    fun getStockList(){
        viewModelScope.launch {
            val savedList = saveStockRepository.getStockList()
            _savedStocks.postValue(savedList)
        }
    }
}
