package com.example.finnub.data.api

import com.example.finnub.data.api.models.SearchResponse
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.domain.SearchRepository
import com.example.finnub.utils.Resourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: ApiRequests
):SearchRepository{
    override suspend fun searchStocks(stockName: String): Flow<Resourse<List<SimpleStock>>> = flow {
        emit(Resourse.Loading())
        try {
             val connect = api.searchStocks(stockName)
             if (connect.isSuccessful){
                 connect.body()?.let { stocksList ->
                     val stockList = stocksList.result.map {
                         SimpleStock(
                             it.symbol,
                             0.0
                         )
                     }
                     emit(Resourse.Success(response = stockList))
                 }
             }else{
                 if (connect.code() == 429)
                     emit(Resourse.Error("\n Too Many Requests, please wait 10-30 seconds and restart"))
                 else
                     emit(Resourse.Error("\n " +
                             "code ${connect.code()} ${connect.message()}"))
             }
         }catch (e:Exception){
             emit(Resourse.Error("\n Check your Internet connection and retry download"))
         }
    }


}