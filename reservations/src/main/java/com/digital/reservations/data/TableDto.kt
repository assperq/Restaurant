package com.digital.reservations.data

import com.digital.reservations.domain.TableStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TableDto(
    @SerialName("id") val id : Int,
    @SerialName("waiter_id") val waiterId : Int,
    @SerialName("virtual_status") val status : String,
    @SerialName("x") val x : Int,
    @SerialName("y") val y : Int
)

@Serializable
data class TodayTableDto(
    @SerialName("id") val id : Int,
    @SerialName("waiter_id") val waiterId : Int,
    @SerialName("status") val status : String,
    @SerialName("x") val x : Int,
    @SerialName("y") val y : Int
)
