package com.digital.statistics.presentation.navigation

sealed class StatisticsRoutes(val route : String) {
    data object StatisticsRoute : StatisticsRoutes("statistics_route_main")
}