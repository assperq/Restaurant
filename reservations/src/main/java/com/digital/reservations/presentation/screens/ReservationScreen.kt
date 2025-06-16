package com.digital.reservations.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.digital.reservations.presentation.ReservationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReservationScreen(
    reservationViewModel: ReservationViewModel = koinViewModel()
) {
    val tables = reservationViewModel.tables.collectAsState()
    Text(tables.value.toString())
}