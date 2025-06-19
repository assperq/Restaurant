package com.digital.profile.presentation.screens

import android.R.attr.order
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.profile.domain.OrderDetail
import com.digital.profile.domain.OrderStatus
import com.digital.profile.domain.Profile
import com.digital.profile.domain.UserRole
import com.digital.profile.presentation.getDateFromInstant
import kotlinx.datetime.format
import java.time.format.DateTimeFormatter

@Composable
fun OrderDetailDialog(
    order: OrderDetail?,
    profile: Profile,
    onDismiss: () -> Unit,
    onUpdateStatus: (OrderStatus, Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            if (order != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Заказ №${order.orderId}", style = MaterialTheme.typography.titleLarge)
                    StatusBadge(order.status)
                }
            }
        },
        text = {
            if (order == null) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text("Дата: ${getDateFromInstant(order.orderDate)}")
                    order.waiterName?.let {
                        Text("Официант: $it")
                    }

                    HorizontalDivider()

                    Text("Блюда:", style = MaterialTheme.typography.titleMedium)

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        order.dishes.forEach { dish ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(dish.name)
                                    Text("x${dish.quantity}")
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    Text(
                        "Итого: ${order.totalCost} ₽",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (profile.role == UserRole.ADMIN && order.status != OrderStatus.GIVEN) {
                        Spacer(Modifier.height(12.dp))
                        Text("Изменить статус:", style = MaterialTheme.typography.bodyLarge)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (order.status == OrderStatus.CREATED) {
                                Button(onClick = {
                                    onUpdateStatus(OrderStatus.READY, order.orderId)
                                    onDismiss()
                                }) {
                                    Text("Отметить как \"Готов\"")
                                }
                            }
                            if (order.status == OrderStatus.READY) {
                                Button(onClick = {
                                    onUpdateStatus(OrderStatus.GIVEN, order.orderId)
                                    onDismiss()
                                }) {
                                    Text("Отметить как \"Выдан\"")
                                }
                            }
                        }
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}

@Composable
fun StatusBadge(status: OrderStatus) {
    val (label, color) = when (status) {
        OrderStatus.CREATED -> "Создан" to Color.Gray
        OrderStatus.READY -> "Готов" to Color(0xFFFFC107)
        OrderStatus.GIVEN -> "Выдан" to Color(0xFF4CAF50)
    }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(label, color = Color.White, fontSize = 12.sp)
    }
}