package com.digital.reservations.presentation.screens.hall

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.reservations.domain.Table
import com.digital.reservations.domain.TableStatus
import androidx.compose.ui.graphics.Color
import com.digital.reservations.presentation.screens.BaseText
import com.digital.reservations.presentation.screens.DefaultButton
import com.digital.reservations.presentation.screens.reservation.ChairView

@Composable
fun TableInHallDetailsView(
    table: Table,
    tableSize: Dp = 160.dp,
    chairSize: Dp = 32.dp,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    onReserveClick : (Table) -> Unit
) {
    val backgroundColor = when (table.status) {
        TableStatus.FREE -> colorScheme.secondary
        TableStatus.BUSY -> colorScheme.primary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(2) {
                ChairView(size = chairSize, color = colorScheme.surfaceVariant)
            }
        }

        Box(
            modifier = Modifier
                .size(tableSize)
                .background(backgroundColor, CircleShape)
                .border(2.dp, colorScheme.outline, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.LocalFlorist,
                    contentDescription = null,
                    tint = colorScheme.onSecondary,
                    modifier = Modifier.size(24.dp)
                )
                BaseText(
                    text = "Стол #${table.id}",
                    color = colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(2) {
                ChairView(size = chairSize, color = colorScheme.surfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BaseText("Cтатус стола:", color = MaterialTheme.colorScheme.onBackground, fontSize = 22.sp)
            BaseText(table.status.russianName, color = MaterialTheme.colorScheme.onBackground, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        DefaultButton(
            onClick = { onReserveClick(table) },
        ) {
            BaseText("Изменить статус стола", color = colorScheme.onPrimary)
        }
    }
}

@Composable
fun ChairView(size: Dp, color:Color) {
    Box(
        modifier = Modifier
            .size(size)
            .background(color, CircleShape)
            .border(1.dp, colorScheme.outline, CircleShape)
    )
}