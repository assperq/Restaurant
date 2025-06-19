package com.digital.order.data

import kotlinx.serialization.Serializable

@Serializable
data class DishOrderDto(
    val dish_id: Int,
    val quantity: Int
)

@Serializable
data class OrderResultDto(
    val success: Boolean,
    val order_id: Int? = null,
    val error: String? = null,
    val missing: List<MissingItemDto>? = null
)

@Serializable
data class MissingItemDto(
    val dish_id: Int,
    val required: Int,
    val available: Int
)
