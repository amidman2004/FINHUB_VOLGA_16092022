package com.example.finnub.utils.extensionmethods

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.example.finnub.data.api.models.SimpleStock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

suspend fun MutableStateFlow<List<SimpleStock>>.savePage(
    page:List<SimpleStock>,
    currentPage:Int
){
    try {
        val saveList = this.value.apply {
            for (i in page.indices){
                val index = i + page.size*(currentPage - 1)
                this[index].price = page[i].price
            }
        }
        value = saveList
    }catch (e:Exception){

    }
}