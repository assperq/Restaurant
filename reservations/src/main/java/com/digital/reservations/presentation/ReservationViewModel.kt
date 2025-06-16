package com.digital.reservations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.reservations.domain.ReservationRepository
import com.digital.reservations.domain.Table
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class ReservationViewModel(
    private val repository: ReservationRepository
) : ViewModel() {

    private val _tables : MutableStateFlow<List<Table>> = MutableStateFlow(emptyList())
    val tables = _tables.asStateFlow()

    init {
        val tomorrow = Clock.System.now().plus(DateTimePeriod(days = 1), TimeZone.currentSystemDefault())
        fetchTables(tomorrow)
    }

    fun fetchTables(date : Instant) {
        viewModelScope.launch {
            _tables.value = repository.getTables(date)
        }
    }
}