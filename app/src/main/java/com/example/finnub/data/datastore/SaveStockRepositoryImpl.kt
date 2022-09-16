package com.example.finnub.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.domain.SaveStockRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class SaveStockRepositoryImpl @Inject constructor(
    private val context: Context
):SaveStockRepository {

    private val stockListName = stringPreferencesKey(DataStoreConstants.dataStoreList)

    override suspend fun addStock(stock:SimpleStock) {
        try{
            val currentValue = context.stockDataStore.data.map {
                it[stockListName]
            }.first()?.toSimpleStockList() ?: mutableListOf()

            currentValue.add(stock)

            val jsonList = currentValue.toJson()

            context.stockDataStore.edit {
                it[stockListName] = jsonList
            }
        }catch (e:Exception){

        }
    }

    override suspend fun getStockList(): List<SimpleStock> {
        return try{
            context.stockDataStore.data.map {
                it[stockListName]
            }.first()?.toSimpleStockList() ?: mutableListOf()
        }catch (e:Exception){
            mutableListOf()
        }
    }

    override suspend fun deleteFromStockList(stockName: String) {
        try{
            val currentValue = context.stockDataStore.data.map {
                it[stockListName]
            }.first()?.toSimpleStockList() ?: mutableListOf()

            val stock = currentValue.find {
                it.symbol == stockName
            }
            currentValue.remove(stock)

            val jsonList = currentValue.toJson()

            context.stockDataStore.edit {
                it[stockListName] = jsonList
            }
        }catch (e:Exception){

        }
    }





}


@OptIn(ExperimentalStdlibApi::class)
fun String.toSimpleStockList():MutableList<SimpleStock>{
    return Gson().fromJson(this, typeOf<MutableList<SimpleStock>>().javaType)
}

fun MutableList<SimpleStock>.toJson():String{
    return Gson().toJson(this)
}