package com.digital.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.profile.domain.StatisticsRepository
import com.digital.profile.domain.WaiterStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class StatisticsViewModel(
    private val repository: StatisticsRepository
) : ViewModel() {

    var waiterStats by mutableStateOf<List<WaiterStats>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

    fun fetchWaiterStatsForMonth(date: LocalDate) {
        viewModelScope.launch {
            isLoading = true
            val stats = repository.loadStatistics(date)
            waiterStats = stats
            isLoading = false
        }
    }
}