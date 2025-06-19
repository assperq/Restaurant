package com.digital.restaraunt.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.digital.order.presentation.navigation.OrderRoutes
import com.digital.registration.presentation.navigation.AuthRoutes
import com.digital.reservations.presentation.navigation.ReservationRoutes
import com.digital.reservations.presentation.screens.ReservationScreen

@Composable
fun BottomBar(navController : NavController) {
    val items = listOf(
        NavItem("Бронирование", ReservationRoutes.Reservation.route, Icons.Default.CalendarMonth),
        NavItem("Меню", OrderRoutes.MainOrderRoute.route, Icons.Default.RestaurantMenu),
        NavItem("Профиль", AuthRoutes.MainRoute.route, Icons.Default.Person)
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        tint = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
            )
        }
    }
}

data class NavItem(val label: String, val route: String, val icon: ImageVector)