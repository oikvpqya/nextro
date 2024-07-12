package me.oikvpqya.apps.music.model

sealed class AppDefaultResult<T> {
    class Nothing<T> : AppDefaultResult<T>()
    class Loading<T> : AppDefaultResult<T>()
    data class Success<T>(val data: T) : AppDefaultResult<T>()
    data class Failure<T>(val e: Throwable) : AppDefaultResult<T>()
}
