package com.digital.order.presentation.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chargemap.compose.numberpicker.ListItemPicker
import com.digital.reservations.domain.Table


@Composable
fun TableSelectionDialog(
    tables: List<Table>,
    selectedTable: Table?,
    onTableSelected: (Table) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmEnabled: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите стол") },
        text = {
            ListItemPicker(
                value = selectedTable,
                list = tables,
                label = {
                    "Номер стола: ${it?.id ?: "1"}"
                },
                onValueChange = {
                    onTableSelected(it!!)
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = confirmEnabled
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}