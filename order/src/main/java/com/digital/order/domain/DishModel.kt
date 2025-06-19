package com.digital.order.domain

data class DishModel(
    val id : Int,
    val name : String,
    val price : Double,
    val availableQuantity : Int,
    val imageUrl  : String
)
