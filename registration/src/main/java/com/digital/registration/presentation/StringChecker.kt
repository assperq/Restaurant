package com.digital.registration.presentation

object StringChecker {
    /**
     * Проверяет строку на соответствие email
     *
     * @return если почта соответствует нужной возвращает true
     */
    fun checkMailString(email : String) : Boolean {
        if (Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(email)) {
            return true
        }
        else {
            throw Exception("Введен неверный email")
        }
    }

    /**
     * Проверяет пароль на длину
     *
     * @return если пароль соответствует трубованиям true
     */
    fun checkPassword(password: String) : Boolean {
        if (password.length >= 6) {
            return true
        }
        else {
            throw Exception("Пароль должен содержать более 6-ти символов")
        }
    }
}