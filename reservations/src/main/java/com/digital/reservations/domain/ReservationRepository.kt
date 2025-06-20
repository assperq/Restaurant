package com.digital.reservations.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

interface ReservationRepository {
    suspend fun getTables(date : LocalDate) : List<Table>
    suspend fun getTablesToday() : List<Table>
    suspend fun updateTableStatus(tableId : Int, tableStatus: TableStatus)
    suspend fun reserveTable(tableId: Int, userId: String, peopleCount: Int, date: LocalDate) : Boolean
}