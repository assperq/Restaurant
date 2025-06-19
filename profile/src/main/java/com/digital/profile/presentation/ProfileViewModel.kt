package com.digital.profile.presentation

import android.R.attr.value
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _user : MutableStateFlow<Profile?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    private val _userReservations : MutableStateFlow<List<ReservationModel>> = MutableStateFlow(emptyList())
    val userReservations = _userReservations.asStateFlow()

    private val _userOrders : MutableStateFlow<List<UserOrder>> = MutableStateFlow(emptyList())
    val userOrders = _userOrders.asStateFlow()

    private val _orderDetails : MutableStateFlow<OrderDetail?> = MutableStateFlow(null)
    val orderDetails = _orderDetails.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _user.value = repository.getCurrentUser()
            fetchUserReservations()
            fetchUserOrders()
        }
    }

    fun fetchUserReservations() {
        viewModelScope.launch {
            _user.value?.let {
                _userReservations.value = repository.getUserReservations(it.id)
            }
        }
    }

    fun fetchUserOrders() {
        viewModelScope.launch {
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

    fun updateReservationStatus(reservationId : Int, status: ReservationStatus) {
        viewModelScope.launch {
            repository.updateReservationStatus(reservationId, status)
            fetchUserReservations()
        }
    }

    fun updateOrderStatus(orderId: Int, orderStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, orderStatus)
            fetchUserOrders()
        }
    }

    fun fetchOrderDetail(orderId : Int) {
        viewModelScope.launch {
            _orderDetails.value = repository.getOrderDetails(orderId)
        }
    }

    fun clearOrderDetails() {
        _orderDetails.value = null
    }

    fun clearUser() {
        viewModelScope.launch {
            _user.value = null
            repository.clearCache()
        }
    }
}