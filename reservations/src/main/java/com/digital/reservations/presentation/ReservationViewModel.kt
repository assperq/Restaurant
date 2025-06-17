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
    private val _state : MutableStateFlow<State> = MutableStateFlow(State.Default)
    val state = _state.asStateFlow()

    sealed class State() {
        data object Default : State()
        data class Success(val list : List<Table>) : State()
        data object Loading : State()
    }

    init {
        val tomorrow = System.now().plus(DateTimePeriod(days = 1), TimeZone.currentSystemDefault())
        fetchTables(tomorrow)
    }

    fun fetchTables(date : Instant) {
        viewModelScope.launch {
            _state.value = State.Loading
            _state.value = State.Success(repository.getTables(date))
        }
    }

    fun reserveTable(tableId : Int, userId : String, peopleCount : Int, date : Instant) {
        viewModelScope.launch {
            val res = repository.reserveTable(tableId, userId, peopleCount, date)
        }
    }
}