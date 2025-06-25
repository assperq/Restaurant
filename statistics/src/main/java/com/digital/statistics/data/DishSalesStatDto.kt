package com.digital.statistics.data

import com.digital.statistics.domain.DishStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishSalesStats(
    @SerialName("dish_name")
    val dishName: String,
    @SerialName("month_1")
    val month1: String,
    @SerialName("month_2")
    val month2: String,
    @SerialName("quantity_1")
    val quantity1: Int,
    @SerialName("quantity_2")
    val quantity2: Int,
    @SerialName("quantity_diff")
    val quantityDiff: Int
) {
    fun toDishStat() : DishStats {
        return DishStats(
            dishName = this.dishName,
            month1 = this.month1,
            month2 = this.month2,
            quantity1 = this.quantity1,
            quantity2 = this.quantity2,
            quantityDiff = this.quantityDiff
        )
    }
}