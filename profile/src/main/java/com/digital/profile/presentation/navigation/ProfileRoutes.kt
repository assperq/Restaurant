package com.digital.profile.presentation.navigation

sealed class ProfileRoutes(val route : String) {
    data object ProfileRoute : ProfileRoutes("profile")
}