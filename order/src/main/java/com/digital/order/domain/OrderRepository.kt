package com.digital.order.domain

interface OrderRepository {
    suspend fun makeOrder(
        tableId: Int,
        userId: String?,
        dishes: List<DishOrderModel>
    ) : OrderResult

    suspend fun getDishesList() : List<DishModel>
}