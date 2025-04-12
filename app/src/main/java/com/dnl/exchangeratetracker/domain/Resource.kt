package com.dnl.exchangeratetracker.domain

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>

    sealed interface ErrorType: Resource<Nothing> {
        data object Network : ErrorType
        data object EmptyResponse : ErrorType
        data object Unknown: ErrorType
    }
}