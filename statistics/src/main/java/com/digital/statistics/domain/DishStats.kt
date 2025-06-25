package com.digital.statistics.domain

data class DishStats(
    val dishName: String,
    val month1: String,
    val month2: String,
    val quantity1: Int,
    val quantity2: Int,
    val quantityDiff: Int
)
