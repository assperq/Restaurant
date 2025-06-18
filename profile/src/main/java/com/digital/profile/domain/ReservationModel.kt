package com.digital.profile.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ReservationModel(
    val id: Int,
    val tableId: Int,
    val status: ReservationStatus,
    val reservationDate: LocalDateTime,
    val peopleCount: Int
)

enum class ReservationStatus(val russianName : String) {
    RESERVED("Зарезервирован"),
    CANCELLED("Отменен"),
    COMPLETED("Закончен"),
}
