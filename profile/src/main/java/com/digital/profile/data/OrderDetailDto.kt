package com.digital.profile.data

import com.digital.profile.domain.OrderStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailRow(
    @SerialName("order_id") val orderId: Int,
    @SerialName("order_date") val orderDate: Instant,
    val status: String,
    @SerialName("total_cost") val totalCost: Double,
    @SerialName("waiter_name") val waiterName: String? = null,
    @SerialName("dish_name") val dishName: String,
    val quantity: Int
)
