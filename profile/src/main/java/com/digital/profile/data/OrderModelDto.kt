package com.digital.profile.data

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class OrderModelDto(
    val id: Int,
    val table_id: Int,
    val waiter_id: Int,
    val user_id: String?,
    val order_date: Instant,
    val total_cost: Double,
    val status : String
)