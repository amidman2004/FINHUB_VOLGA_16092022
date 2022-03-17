package com.example.finnub.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun collectFlows(lifecycle: Lifecycle, lifecycleScope: LifecycleCoroutineScope,
                 onCollect: suspend CoroutineScope.() -> Unit){
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            onCollect()
        }
    }
}