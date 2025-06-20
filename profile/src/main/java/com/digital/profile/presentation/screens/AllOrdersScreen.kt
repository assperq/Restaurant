package com.digital.profile.presentation.screens

import android.R.attr.order
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digital.profile.presentation.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllOrdersScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack : () -> Unit
) {
    val profile = profileViewModel.user.collectAsState()
    val orders = profileViewModel.userOrders.collectAsState()
    var selectedOrder = profileViewModel.orderDetails.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(bottom = 8.dp)) {
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
            items (orders.value) { order ->
                OrderCard(
                    order,
                    onClick = {
                        showDialog = true
                        profileViewModel.fetchOrderDetail(order.id)
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp)
                )
            }
        }
    }

    if (showDialog) {
        OrderDetailDialog(
            order = selectedOrder.value,
            profile = profile.value!!,
            onDismiss = { profileViewModel.clearOrderDetails(); showDialog = false },
            onUpdateStatus = { newStatus, orderId ->
                profileViewModel.updateOrderStatus(orderId, newStatus)
            }
        )
    }
}