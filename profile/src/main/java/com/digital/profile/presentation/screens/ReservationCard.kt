package com.digital.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.presentation.getDateFromInstant
import com.digital.profile.presentation.getTimeFromInstant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import java.time.format.DateTimeFormatter

@Composable
fun ReservationCard(
    reservation: ReservationModel,
    onClick: () -> Unit,
    modifier: Modifier? = null
) {
    val statusColor = statusColor(reservation.status)
    val icon = when (reservation.status) {
        ReservationStatus.RESERVED -> Icons.Default.Event
        ReservationStatus.CANCELLED -> Icons.Default.Cancel
        ReservationStatus.COMPLETED -> Icons.Default.CheckCircle
    }

    Card(
        modifier = modifier?.height(120.dp)?.clickable { onClick() }
            ?: Modifier
                .width(180.dp)
                .height(120.dp)
                .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Стол №${reservation.tableId}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Дата: ${getDateFromInstant(reservation.reservationDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = reservation.status.russianName,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun statusColor(status: ReservationStatus): Color {
    return when (status) {
        ReservationStatus.RESERVED -> Color(0xFF1976D2)    // Синий
        ReservationStatus.CANCELLED -> Color(0xFFD32F2F)   // Красный
        ReservationStatus.COMPLETED -> Color(0xFF388E3C)   // Зелёный
    }
}