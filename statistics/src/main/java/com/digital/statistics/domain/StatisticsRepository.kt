package com.digital.statistics.domain

import com.digital.statistics.domain.WaiterStats
import kotlinx.datetime.LocalDate

interface StatisticsRepository {
    suspend fun loadWaiterStatistics(date : LocalDate) : List<WaiterStats>
    suspend fun loadDishesStatistics(date: LocalDate) : List<DishStats>
}