package com.example.githubinfoservice.network

sealed class Resource<out T> {
    data class Success<out T>(val output: T) : Resource<T>()
    data class Error(val errorMessage: ErrorResponse?) : Resource<Nothing>()
}