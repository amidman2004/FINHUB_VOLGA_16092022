package com.example.finnub.utils

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun collectFlows(lifecycleOwner: LifecycleOwner,
                 onCollect: suspend CoroutineScope.() -> Unit){
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            onCollect()
        }
    }
}