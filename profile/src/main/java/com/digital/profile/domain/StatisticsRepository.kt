package com.digital.profile.domain

import kotlinx.datetime.LocalDate

interface StatisticsRepository {
    suspend fun loadStatistics(date : LocalDate) : List<WaiterStats>
}