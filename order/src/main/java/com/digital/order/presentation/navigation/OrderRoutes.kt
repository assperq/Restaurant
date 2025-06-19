package com.digital.order.presentation.navigation

sealed class OrderRoutes(val route : String) {
    data object MainOrderRoute : OrderRoutes("mainOrderRoute")
    data object CartRoute : OrderRoutes("cartRoute")
}