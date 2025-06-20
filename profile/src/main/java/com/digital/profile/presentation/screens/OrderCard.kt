package com.digital.profile.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.digital.profile.domain.OrderStatus
import com.digital.profile.domain.UserOrder
import com.digital.profile.presentation.getDateFromInstant

@Composable
fun OrderCard(
    order: UserOrder,
    onClick: () -> Unit,
    modifier: Modifier? = null
) {
    val (bgColor, statusLabel, icon) = when (order.status) {
        OrderStatus.CREATED -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            "Создан",
            Icons.Default.HourglassBottom
        )
        OrderStatus.READY -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            "Готов",
            Icons.Default.NotificationsActive
        )
        OrderStatus.GIVEN -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            "Выдан",
            Icons.Default.CheckCircle
        )
    }

    Card(
        modifier = modifier?.height(120.dp)?.clickable { onClick() }
            ?: Modifier
                .width(180.dp)
                .height(120.dp)
                .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Text(
                text = "№${order.id}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Column {
                Text(
                    text = "Сумма: ${order.totalCost} ₽",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = getDateFromInstant(order.orderDate),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}