package com.example.finnub.utils

sealed class LoadingState() {
    object LoadingSuccess: LoadingState()
    object LoadingError: LoadingState()
    object LoadingStart: LoadingState()
    object LoadingInProcess: LoadingState()
    object LoadingNothing: LoadingState()
}