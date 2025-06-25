package com.digital.statistics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.statistics.domain.DishStats
import com.digital.statistics.domain.StatisticsRepository
import com.digital.statistics.domain.WaiterStats
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class StatisticsViewModel(
    private val repository: StatisticsRepository
) : ViewModel() {

    var waiterStats by mutableStateOf<List<WaiterStats>>(emptyList())
        private set

    var dishStats by mutableStateOf<List<DishStats>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

    fun fetchWaiterStatsForMonth(date: LocalDate) {
        viewModelScope.launch {
            isLoading = true
            waiterStats = repository.loadWaiterStatistics(date)
            isLoading = false
        }
    }

    fun fetchDishStatsForMonth(date : LocalDate) {
        viewModelScope.launch {
            isLoading = true
            dishStats = repository.loadDishesStatistics(date)
            isLoading = false
        }
    }
}