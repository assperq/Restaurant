package com.digital.profile.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

data class OrderDetail(
    val orderId: Int,
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    val totalCost: Long,
    val waiterName: String?,
    val dishes: List<DishEntry>
)

data class DishEntry(
    val name: String,
    val quantity: Int
)