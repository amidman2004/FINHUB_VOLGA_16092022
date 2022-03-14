package com.example.finnub.utils

sealed class LoadingState(val error:String = "") {
    object LoadingSuccess: LoadingState()
    class LoadingError(error: String): LoadingState(error = error)
    object LoadingStart: LoadingState()
    object LoadingInProcess: LoadingState()
    object LoadingNothing: LoadingState()
}