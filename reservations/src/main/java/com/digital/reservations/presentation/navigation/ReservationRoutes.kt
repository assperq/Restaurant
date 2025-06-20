package com.digital.reservations.presentation.navigation

sealed class ReservationRoutes(val route : String) {
    data object Reservation : ReservationRoutes("reservation")
    data object Hall : ReservationRoutes("hall")
}