package com.digital.profile.domain

interface ProfileRepository {
    suspend fun getCurrentUser() : Profile?
    suspend fun getUserReservations(userId : String) : List<ReservationModel>
    suspend fun updateReservationStatus(reservationId : Int, status: ReservationStatus)
    fun clearCache()
}