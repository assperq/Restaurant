package com.digital.reservations.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.reservations.domain.Table
import com.digital.reservations.domain.TableStatus

@Composable
fun TableItem(
    table : Table,
    tableSize : Dp,
    chairSize : Dp,
    onTableClick : (Table) -> Unit,
    colorScheme : ColorScheme = MaterialTheme.colorScheme,
) {
    val tableCenterX = table.x.dp
    val tableCenterY = table.y.dp
    val chairDistance = 36.dp

    val backgroundColor = when (table.status) {
        TableStatus.FREE -> colorScheme.secondary
        TableStatus.BUSY -> colorScheme.primary
    }

    val contentColor = when (table.status) {
        TableStatus.FREE -> colorScheme.onSecondary
        TableStatus.BUSY -> colorScheme.onPrimary
    }

    val directions = listOf(
        Pair(0f, -1f),
        Pair(1f, 0f),
        Pair(0f, 1f),
        Pair(-1f, 0f)
    )

    directions.forEach { (dx, dy) ->
        Box(
            modifier = Modifier
                .size(chairSize)
                .offset(
                    x = tableCenterX + (chairDistance * dx) - (chairSize / 2),
                    y = tableCenterY + (chairDistance * dy) - (chairSize / 2)
                )
                .background(colorScheme.surfaceVariant, CircleShape)
                .border(1.dp, colorScheme.outline, CircleShape)
        )
    }

    Box(
        modifier = Modifier
            .size(tableSize)
            .offset(
                x = tableCenterX - (tableSize / 2),
                y = tableCenterY - (tableSize / 2)
            )
            .background(backgroundColor, CircleShape)
            .clickable(enabled = table.status == TableStatus.FREE) {
                onTableClick(table)
            }
            .border(2.dp, colorScheme.outline, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LocalFlorist,
                contentDescription = "Decor",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            BaseText(
                text = table.id.toString(),
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}