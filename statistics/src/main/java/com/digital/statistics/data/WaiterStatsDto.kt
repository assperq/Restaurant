package com.digital.statistics.data

import com.digital.statistics.domain.WaiterStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

@Serializable
data class WaiterStatsDto(
    @SerialName("period") val period: String,
    @SerialName("waiter_last_name") val waiterLastName: String,
    @SerialName("waiter_first_name") val waiterFirstName: String,
    @SerialName("orders_count") val ordersCount: Int,
    @SerialName("paid_checks_count") val paidChecksCount: Int,
    @SerialName("paid_checks_sum") val paidChecksSum: Double,
    @SerialName("orders_diff") val ordersDiff: Int?,
    @SerialName("checks_diff") val checksDiff: Int?,
    @SerialName("sum_diff") val sumDiff: Double?
)

fun WaiterStatsDto.toWaiterStats(): WaiterStats {
    val fullName = "$waiterLastName $waiterFirstName"
    var isImproving = false
    if (ordersDiff != null && checksDiff != null && sumDiff != null) {
        isImproving = ordersDiff >= 0 && checksDiff >= 0 && sumDiff >= 0
    }
    val (year, month) = period.split("-").map { it.toInt() }
    val periodDate = LocalDate(year, month, 1)
    return WaiterStats(
        periodDate = periodDate,
        fullName = fullName,
        ordersCount = ordersCount,
        paidChecksCount = paidChecksCount,
        paidChecksSum = paidChecksSum,
        ordersDiff = ordersDiff,
        checksDiff = checksDiff,
        sumDiff = sumDiff,
        isImproving = isImproving
    )
}

