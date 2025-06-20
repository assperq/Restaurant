package com.digital.reservations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.presentation.ProfileViewModel
import com.digital.reservations.domain.ReservationRepository
import com.digital.reservations.domain.Table
import com.digital.reservations.domain.TableStatus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.core.KoinApplication.Companion.init

class ReservationViewModel(
    private val repository: ReservationRepository,
    private val profileViewModel: ProfileViewModel
) : ViewModel() {
    private val _state : MutableStateFlow<State> = MutableStateFlow(State.Default)
    val state = _state.asStateFlow()

    sealed class State() {
        data object Default : State()
        data class Success(val list : List<Table>, val action : String? = null) : State()
        data class Error(val throwable: Throwable) : State()
        data object Loading : State()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = State.Error(throwable)
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    init {
        val tomorrow = System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(1, DAY)
        fetchTables(tomorrow)
    }

    fun fetchTables(date : LocalDate) {
        coroutineScope.launch {
            _state.value = State.Loading
            _state.value = State.Success(repository.getTables(date))
        }
    }

    fun fetchTablesToday() {
        coroutineScope.launch {
            _state.value = State.Loading
            _state.value = State.Success(repository.getTablesToday())
        }
    }

    fun changeTableStatus(tableId : Int, status: TableStatus) {
        coroutineScope.launch {
            _state.value = State.Loading
            repository.updateTableStatus(tableId, status)
            _state.value = State.Success(repository.getTablesToday())
        }
    }

    fun reserveTable(tableId : Int, peopleCount : Int, date : LocalDate) {
        coroutineScope.launch {
            _state.value = State.Loading
            val user = profileViewModel.user.value
            if (user == null) {
                throw Exception("Вы еще не вошли в аккаунт")
            }
            if (repository.reserveTable(tableId, user.id, peopleCount, date)) {
                profileViewModel.fetchUserReservations()
                _state.value = State.Success(repository.getTables(date), "reserve")
            }
            else {
                throw Exception("К сожалению стол забронировали чуть раньше вас")
            }
        }
    }

    fun clearError() {
        _state.value = State.Default
    }
}