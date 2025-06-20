package com.digital.profile.presentation.navigation

sealed class ProfileRoutes(val route : String) {
    data object AllReservationRoute : ProfileRoutes("allReservations")
    data object AllOrdersRoute : ProfileRoutes("allOrders")
}