package com.digital.restaraunt.navigation


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.digital.order.presentation.navigation.OrderRoutes
import com.digital.order.presentation.screens.OrderScreen
import com.digital.profile.presentation.ProfileViewModel
import com.digital.profile.presentation.navigation.ProfileRoutes
import com.digital.profile.presentation.screens.AllOrdersScreen
import com.digital.profile.presentation.screens.AllReservationsScreen
import com.digital.profile.presentation.screens.ProfileScreen
import com.digital.registration.presentation.AuthViewModel
import com.digital.registration.presentation.navigation.AuthRoutes
import com.digital.registration.presentation.screens.ErrorDialog
import com.digital.registration.presentation.screens.LoginScreen
import com.digital.registration.presentation.screens.MainAuthScreen
import com.digital.registration.presentation.screens.RegistrationScreen
import com.digital.reservations.presentation.navigation.ReservationRoutes
import com.digital.reservations.presentation.screens.hall.HallScreen
import com.digital.reservations.presentation.screens.reservation.ReservationScreen
import com.digital.statistics.presentation.StatisticsScreen
import com.digital.statistics.presentation.navigation.StatisticsRoutes
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupNavGraph(
    navController : NavHostController,
    authViewModel : AuthViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinInject()
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    val user = profileViewModel.user.collectAsState()

    var dialogText by remember {
        mutableStateOf("")
    }

    if (showDialog) {
        ErrorDialog(
            message = dialogText,
            title = "Ошибка авторизации",
            onClickOk = {
                showDialog = false
                authViewModel.clearError()
            },
            onDismiss = {
                showDialog = false
                authViewModel.clearError()
            })
    }

    var showLoadingDialog by remember {
        mutableStateOf(false)
    }

    if (showLoadingDialog) {
        Dialog(
            onDismissRequest = {}
        ) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(authViewModel) {
        authViewModel.state.collect {
            when(it) {
                AuthViewModel.State.DefaultState -> {}
                is AuthViewModel.State.Error -> {
                    showLoadingDialog = false
                    showDialog = true
                    dialogText = supabaseErrorDeterminant(it.error)
                }
                is AuthViewModel.State.Success -> {
                    showLoadingDialog = false
                    navController.navigate(AuthRoutes.MainRoute.route) {
                        popUpTo(it.route) { inclusive = true }
                        popUpTo(AuthRoutes.MainRoute.route) { inclusive = true }
                    }
                }
                AuthViewModel.State.Loading -> {
                    showLoadingDialog = true
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = ReservationRoutes.Reservation.route) {
        composable(ReservationRoutes.Reservation.route) {
            ReservationScreen()
        }

        composable(AuthRoutes.MainRoute.route) {
            if (user.value == null) {
                MainAuthScreen(
                    onLoginScreenNavigate = {
                        navController.navigate(AuthRoutes.SignInRoute.route)
                    },
                    onRegistrationScreenNavigate = {
                        navController.navigate(AuthRoutes.SignUpRoute.route)
                    }
                )
            }
            else {
                ProfileScreen(
                    profile = user.value!!,
                    onLogout = {
                        authViewModel.signOut()
                    },
                    onViewAllReservations = {
                        navController.navigate(ProfileRoutes.AllReservationRoute.route)
                    },
                    onViewAllOrders = {
                        navController.navigate(ProfileRoutes.AllOrdersRoute.route)
                    },
                    onStatisticsScreen = {
                        navController.navigate(StatisticsRoutes.StatisticsRoute.route)
                    }
                )
            }
        }

        composable(AuthRoutes.SignInRoute.route) {
            LoginScreen(onNavigateBack = {
                navController.navigateUp()
            }, authViewModel)
        }

        composable(AuthRoutes.SignUpRoute.route) {
            RegistrationScreen(onNavigateBack = {
                navController.navigateUp()
            }, authViewModel)
        }

        composable(ProfileRoutes.AllReservationRoute.route) {
            AllReservationsScreen {
                navController.navigateUp()
            }
        }

        composable(OrderRoutes.MainOrderRoute.route) {
            OrderScreen(
                profile = user.value,
                onOrderSuccess = {}
            )
        }

        composable(ProfileRoutes.AllOrdersRoute.route) {
            AllOrdersScreen {
                navController.navigateUp()
            }
        }

        composable(ReservationRoutes.Hall.route) {
            HallScreen()
        }

        composable(StatisticsRoutes.StatisticsRoute.route) {
            StatisticsScreen()
        }
    }
}
