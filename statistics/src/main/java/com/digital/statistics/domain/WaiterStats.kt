package com.digital.statistics.domain

import kotlinx.datetime.LocalDate

data class WaiterStats(
    val periodDate: LocalDate,
    val fullName: String,
    val ordersCount: Int,
    val paidChecksCount: Int,
    val paidChecksSum: Double,
    val ordersDiff: Int?,
    val checksDiff: Int?,
    val sumDiff: Double?,
    val isImproving: Boolean
)