package com.digital.order.presentation.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.digital.reservations.presentation.screens.DefaultButton

@Composable
fun ErrorDialog(message: String, title: String, onClickOk : () -> Unit, onDismiss : () -> Unit) {
    AlertDialog(
        onDismissRequest = { onClickOk() },
        title = { Text(title) },
        text = { Text(message, color = Color.Black, fontSize = 16.sp) },
        confirmButton = {
            DefaultButton(onClick = onClickOk) {
                Text("OK")
            }
        }
    )
}