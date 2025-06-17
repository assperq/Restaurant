package com.digital.restaraunt.navigation

sealed class AppRoutes(val route : String) {
    data object Loading : AppRoutes("loading")
}