package com.digital.order.domain

data class OrderResult(
    val success: Boolean,
    val orderId: Int? = null,
    val error: String? = null,
    val missing: List<MissingItem>? = null
)

data class MissingItem(
    val dishId: Int,
    val required: Int,
    val available: Int
)