package com.digital.restaraunt.navigation

import android.R.id.message
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.ktor.network.sockets.SocketTimeoutException

fun supabaseErrorDeterminant(it : Throwable) : String {
    return when (it) {
        is AuthRestException -> {
            val fullMessage = it.message ?: "Ошибка авторизации"
            if (fullMessage.contains("invalid_credentials")) {
                "Введенные данные не верны"
            }
            else {
                fullMessage.substringBefore(" (").take(100)
            }
        }
        is HttpRequestException -> "Ошибка соединения с сервером"
        is SocketTimeoutException -> "Таймаут соединения"
        else -> "Неизвестная ошибка: ${it.message?.take(100)}"
    }
}