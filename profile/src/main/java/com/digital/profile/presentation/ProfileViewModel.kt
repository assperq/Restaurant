package com.digital.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.profile.domain.Profile
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
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

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _user.value = repository.getCurrentUser()
            fetchUserReservations()
        }
    }

    fun fetchUserReservations() {
        viewModelScope.launch {
            _user.value?.let {
                _userReservations.value = repository.getUserReservations(it.id)
            }
        }
    }

    fun updateReservationStatus(reservationId : Int, status: ReservationStatus) {
        viewModelScope.launch {
            repository.updateReservationStatus(reservationId, status)
            fetchUserReservations()
        }
    }

    fun clearUser() {
        viewModelScope.launch {
            _user.value = null
            repository.clearCache()
        }
    }
}