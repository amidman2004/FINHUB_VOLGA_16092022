package com.example.finnub.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun collectFlows(lifecycle: Lifecycle,lifecycleCoroutineScope: LifecycleCoroutineScope,
                 onCollect: suspend CoroutineScope.() -> Unit){
    lifecycleCoroutineScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
            onCollect()
        }
    }
}