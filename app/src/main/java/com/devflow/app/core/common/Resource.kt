package com.devflow.app.core.common

sealed interface Resource<out T> {

    data class Success<T>(
        val data: T
    ) : Resource<T>

    data class Error(
        val message: String
    ) : Resource<Nothing>
}