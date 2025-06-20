package com.digital.profile.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserRole
import com.digital.profile.presentation.ProfileViewModel
import com.digital.profile.presentation.isAfter
import com.digital.profile.presentation.isBefore
import com.digital.profile.presentation.isToday
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllReservationsScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack : () -> Unit
) {
    val reservations = profileViewModel.userReservations.collectAsState()
    val profile = profileViewModel.user.collectAsState()
    var selectedReservation by remember { mutableStateOf<ReservationModel?>(null) }


    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Все", "Предстоящие", "Прошедшие", "Сегодня")

    Column(modifier = Modifier.fillMaxSize().padding(bottom = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }

            // Вкладки
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.weight(1f),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        val filteredReservations = remember(reservations.value, selectedTab) {
            val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            when (selectedTab) {
                1 -> reservations.value.filter { it.reservationDate.isAfter(currentDateTime) }
                2 -> reservations.value.filter { it.reservationDate.isBefore(currentDateTime) }
                3 -> reservations.value.filter { it.reservationDate.isToday() }
                else -> reservations.value
            }.sortedBy { it.reservationDate }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredReservations) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = { selectedReservation = reservation },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
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
                    selectedReservation = null
                },
                onCompleteReservation = {
                    profileViewModel.updateReservationStatus(reservation.id, ReservationStatus.COMPLETED)
                    selectedReservation = null
                }
            )
        }
    }
}