package com.digital.profile.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserRole

@Composable
fun ReservationStatusDialog(
    selectedReservation : ReservationModel,
    profileRole : UserRole,
    onDismissRequest : () -> Unit,
    onCancelReservation : () -> Unit,
    onCompleteReservation : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Изменить статус") },
        text = {
            Column {
                Button(
                    onClick = {
                        onCancelReservation()
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Отменить бронь")
                }

                if (profileRole == UserRole.ADMIN) {
                    Button(
                        onClick = {
                            onCompleteReservation()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("Отметить как завершённую")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Отмена")
            }
        }
    )
}