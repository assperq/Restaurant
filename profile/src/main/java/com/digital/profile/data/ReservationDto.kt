package com.digital.profile.data

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservationDto(
    val id: Int,
    @SerialName("table_id") val tableId: Int,
    val status: String,
    @SerialName("reservation_date") val reservationDate: Instant,
    @SerialName("people_count") val peopleCount: Int
)
