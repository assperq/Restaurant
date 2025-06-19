package com.digital.profile.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserRole
import com.digital.profile.presentation.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllReservationsScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack : () -> Unit
) {
    val reservations = profileViewModel.userReservations.collectAsState()
    val profile = profileViewModel.user.collectAsState()
    var selectedReservation by remember { mutableStateOf<ReservationModel?>(null) }

    Column {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                null)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items (reservations.value) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = { selectedReservation = reservation },
                    modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp)
                )
            }
        }
    }

    selectedReservation?.let { reservation ->
        if (reservation.status == ReservationStatus.RESERVED) {
            ReservationStatusDialog(
                reservation,
                profileRole = profile.value?.role ?: UserRole.CLIENT,
                onDismissRequest = {
                    selectedReservation = null
                },
                onCancelReservation = {
                    profileViewModel.updateReservationStatus(reservation.id, ReservationStatus.CANCELLED)
                },
                onCompleteReservation = {
                    profileViewModel.updateReservationStatus(reservation.id, ReservationStatus.COMPLETED)
                }
            )
        }
    }
}