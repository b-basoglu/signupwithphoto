package com.bbasoglu.core.network

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T?) : NetworkResponse<T>()
    data class Error<T>(val message: String?,val data: T?=null) : NetworkResponse<T>()
    class Loading<T> : NetworkResponse<T>()
}