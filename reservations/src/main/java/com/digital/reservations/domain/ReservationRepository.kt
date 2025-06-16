package com.digital.reservations.domain

import kotlinx.datetime.Instant

interface ReservationRepository {
    suspend fun getTables(date : Instant) : List<Table>
    suspend fun reserveTable(table : Table, date : Instant) : Boolean
}