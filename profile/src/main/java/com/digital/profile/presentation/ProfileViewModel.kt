package com.digital.profile.presentation

import android.R.attr.value
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.profile.domain.OrderDetail
import com.digital.profile.domain.OrderStatus
import com.digital.profile.domain.Profile
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserOrder
import com.digital.profile.domain.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.KoinApplication.Companion.init

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _user : MutableStateFlow<Profile?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    private val _userReservations : MutableStateFlow<List<ReservationModel>> = MutableStateFlow(emptyList())
    val userReservations = _userReservations.asStateFlow()

    private val _todayReservations : MutableStateFlow<List<ReservationModel>> = MutableStateFlow(emptyList())
    val todayReservations = _todayReservations.asStateFlow()

    private val _userOrders : MutableStateFlow<List<UserOrder>> = MutableStateFlow(emptyList())
    val userOrders = _userOrders.asStateFlow()

    private val _orderDetails : MutableStateFlow<OrderDetail?> = MutableStateFlow(null)
    val orderDetails = _orderDetails.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            _user.value = repository.getCurrentUser()
            if (_user.value?.role == UserRole.ADMIN) {
                fetchTodayReservations()
            }
            fetchUserReservations()
            fetchUserOrders()
        }
    }

    fun fetchUserReservations() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            _user.value?.let {
                _userReservations.value = repository.getUserReservations(it.id)
            }
        }
    }

    fun fetchUserOrders() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            _user.value?.let {
                if (it.role == UserRole.ADMIN) {
                    _userOrders.value = repository.getOrdersToday()
                }
                else {
                    _userOrders.value = repository.getUserOrders(it.id)
                }
            }
        }
    }

    fun fetchTodayReservations() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            _todayReservations.value = repository.getTodayReservations()
        }
    }

    fun updateReservationStatus(reservationId : Int, status: ReservationStatus) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            repository.updateReservationStatus(reservationId, status)
            fetchUserReservations()
            if (_user.value?.role == UserRole.ADMIN) {
                fetchTodayReservations()
            }
        }
    }

    fun updateOrderStatus(orderId: Int, orderStatus: OrderStatus) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            repository.updateOrderStatus(orderId, orderStatus)
            fetchUserOrders()
        }
    }

    fun fetchOrderDetail(orderId : Int) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            _orderDetails.value = repository.getOrderDetails(orderId)
        }
    }

    fun clearOrderDetails() {
        _orderDetails.value = null
    }

    fun clearUser() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            _user.value = null
            repository.clearCache()
        }
    }
}

fun LocalDateTime.isAfter(other: LocalDateTime): Boolean {
    return this > other
}

fun LocalDateTime.isBefore(other: LocalDateTime): Boolean {
    return this < other
}

fun LocalDateTime.isToday(): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return this.date == today
}