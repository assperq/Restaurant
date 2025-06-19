package com.digital.order.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun Counter(
    count: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onRemove,
            enabled = count > 0 && enabled
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
        }

        Text(text = count.toString())

        IconButton(
            onClick = onAdd,
            enabled = enabled
        ) {
            Icon(Icons.Default.Add, contentDescription = "Увеличить")
        }
    }
}