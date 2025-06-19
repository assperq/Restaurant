package com.digital.profile.domain

interface ProfileRepository {
    suspend fun getCurrentUser() : Profile?
    suspend fun getUserReservations(userId : String) : List<ReservationModel>
    suspend fun updateReservationStatus(reservationId : Int, status: ReservationStatus)
    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus)
    suspend fun getUserOrders(userId : String) : List<UserOrder>
    suspend fun getOrdersToday() : List<UserOrder>
    suspend fun getOrderDetails(orderId: Int): OrderDetail
    fun clearCache()
}