package com.amary.sisosmed.core.source.remote.network

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val codeError: Int, val errorMessage: String) : ApiResult<Nothing>()
    object Empty : ApiResult<Nothing>()
}