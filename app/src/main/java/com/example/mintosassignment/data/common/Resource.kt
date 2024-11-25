package com.example.mintosassignment.data.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val errorCode: Int? = null,
    val loading: Boolean? = null
) {

    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String, errorCode: Int? = null, data: T? = null) :
        Resource<T>(data = data, message = message, errorCode = errorCode)

    class Loading<T>(loading: Boolean) : Resource<T>(loading = loading)
}