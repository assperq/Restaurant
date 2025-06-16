package com.digital.reservations.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupReservationNavigation(navController : NavHostController, startDestination : String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ReservationRoutes.Reservation.route) {

        }
    }
}