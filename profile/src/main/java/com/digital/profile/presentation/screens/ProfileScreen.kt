package com.digital.profile.presentation.screens

import android.R.attr.order
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digital.profile.domain.OrderDetail
import com.digital.profile.domain.Profile
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserRole
import com.digital.profile.presentation.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    profile : Profile,
    onLogout : () -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel(),
    onViewAllReservations: () -> Unit,
    onViewAllOrders: () -> Unit
) {
    val reservations = profileViewModel.userReservations.collectAsState()
    val orders = profileViewModel.userOrders.collectAsState()
    val todayReservations = profileViewModel.todayReservations.collectAsState()
    var selectedReservation by remember { mutableStateOf<ReservationModel?>(null) }
    var selectedOrder = profileViewModel.orderDetails.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(profileViewModel) {
        profileViewModel.fetchUserOrders()
        profileViewModel.fetchUserReservations()
        if (profile.role == UserRole.ADMIN) {
            profileViewModel.fetchTodayReservations()
        }
    }

    LazyColumn(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(onClick = onLogout) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, null)
                    }
                }

                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 32.sp
                )
                Text(
                    text = profile.role.russianName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Брони",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onViewAllReservations) {
                        Text("Показать все →", fontSize = 18.sp)
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(100.dp)
                ) {
                    items(reservations.value.take(5)) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            onClick = { selectedReservation = reservation }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (profile.role == UserRole.ADMIN) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Брони на сегодня", style = MaterialTheme.typography.titleSmall,
                            fontSize = 22.sp, modifier = Modifier.weight(1f)
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(100.dp)
                    ) {
                        items(todayReservations.value) { reservation ->
                            ReservationCard(
                                reservation = reservation,
                                onClick = { selectedReservation = reservation }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "История заказов",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onViewAllOrders) {
                        Text("Показать все →", fontSize = 18.sp)
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(orders.value.take(6)) { order ->
                        OrderCard(
                            order,
                            onClick = {
                                profileViewModel.fetchOrderDetail(order.id)
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    selectedReservation?.let { reservation ->
        if (reservation.status == ReservationStatus.RESERVED) {
            ReservationStatusDialog(
                reservation,
                profileRole = profile.role,
                onDismissRequest = { selectedReservation = null },
                onCancelReservation = {
                    profileViewModel.updateReservationStatus(reservation.id, ReservationStatus.CANCELLED)
                },
                onCompleteReservation = {
                    profileViewModel.updateReservationStatus(reservation.id, ReservationStatus.COMPLETED)
                }
            )
        }
    }

    if (showDialog) {
        Log.d("LOG", "USER: $profile")
        OrderDetailDialog(
            order = selectedOrder.value,
            profile = profile,
            onDismiss = { profileViewModel.clearOrderDetails(); showDialog = false },
            onUpdateStatus = { newStatus, orderId ->
                profileViewModel.updateOrderStatus(orderId, newStatus)
            }
        )
    }
}
