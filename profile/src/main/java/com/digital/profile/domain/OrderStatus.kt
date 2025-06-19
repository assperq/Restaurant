package com.digital.profile.domain

enum class OrderStatus(val russianName : String) {
    CREATED("Создан"),
    READY("Готов к выдаче"),
    GIVEN("Выдан")
}