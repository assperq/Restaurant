package com.digital.registration.presentation.navigation

sealed class AuthRoutes(val route : String) {
    data object MainRoute : AuthRoutes("auth")
    data object SignInRoute : AuthRoutes("signIn")
    data object SignUpRoute : AuthRoutes("SignUp")
}