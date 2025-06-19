package com.digital.order.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishDto(
    val id : Int,
    val name : String,
    val price : Double,
    @SerialName("available_quantity") val availableQuantity : Int,
    val imageUrl  : String
)
