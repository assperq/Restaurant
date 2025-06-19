package com.digital.profile.domain

import kotlinx.datetime.LocalDateTime

data class UserOrder(
    val id: Int,
    val tableId: Int,
    val waiterId: Int,
    val userId: String,
    val orderDate: LocalDateTime,
    val totalCost: Long,
    val status : OrderStatus
)
