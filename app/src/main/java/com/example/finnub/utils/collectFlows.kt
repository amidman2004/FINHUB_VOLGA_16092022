package com.example.finnub.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun collectFlows(lifecycleOwner: LifecycleOwner,
                 onCollect: suspend CoroutineScope.() -> Unit){
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            onCollect()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun <T> Flow<T>.collectFlow(){
    val lifeCycleOwner = LocalLifecycleOwner.current

    lifeCycleOwner.lifecycleScope.launch{
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            this@collectFlow.collect()
        }
    }
}