package com.digital.order.presentation.screens

import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.digital.payment.domain.CardData
import com.digital.profile.domain.Profile
import com.digital.profile.domain.UserRole
import com.digital.reservations.domain.Table

@Composable
fun OrderPlacementDialog(
    profile: Profile?,
    tables: List<Table>,
    onDismiss: () -> Unit,
    onSubmit: (table: Table, cardData: CardData?) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedTable by remember { mutableStateOf<Table?>(null) }

    var cardNumber by remember { mutableStateOf(TextFieldValue("")) }
    var validityPeriod by remember { mutableStateOf(TextFieldValue("")) }
    var cvcCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                when (step) {
                    1 -> "Выберите стол"
                    2 -> "Оплата заказа"
                    else -> ""
                }
            )
        },
        text = {
            when (step) {
                1 -> {
                    ListItemPicker(
                        value = selectedTable,
                        list = tables,
                        label = { "Стол №${it?.id}" },
                        onValueChange = { selectedTable = it!! },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                2 -> {
                    if (profile == null || profile.role == UserRole.CLIENT) {

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { input ->
                                    val digits = input.text.filter { it.isDigit() }.take(16)
                                    val formatted = digits.chunked(4).joinToString(" ")

                                    val cursorOffset = formatted.length - cardNumber.text.length +
                                            input.selection.start
                                    cardNumber = TextFieldValue(
                                        text = formatted,
                                        selection = TextRange(cursorOffset.coerceIn(0, formatted.length))
                                    )
                                },
                                label = { Text("Номер карты") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                visualTransformation = VisualTransformation.None,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = validityPeriod,
                                onValueChange = { newValue ->
                                    val digits = newValue.text.filter { it.isDigit() }.take(4)
                                    val formatted = when {
                                        digits.length <= 2 -> digits
                                        else -> "${digits.substring(0, 2)}/${digits.substring(2)}"
                                    }

                                    val cursorOffset = formatted.length - validityPeriod.text.length +
                                            newValue.selection.start

                                    val month = digits.take(2).toIntOrNull() ?: 0
                                    if (month in 1..12 || digits.length < 2) {
                                        validityPeriod = TextFieldValue(
                                            text = formatted,
                                            selection = TextRange(cursorOffset.coerceIn(0, formatted.length))
                                        )
                                    }
                                },
                                label = { Text("Срок действия (MM/YY)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                visualTransformation = VisualTransformation.None,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = cvcCode,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { ch -> ch.isDigit() }) {
                                        cvcCode = it
                                    }
                                },
                                label = { Text("CVC код") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        Text("Подключен терминал оплаты. Подтвердите операцию.")
                    }
                }
            }
        },
        confirmButton = {
            val isCardValid = cardNumber.text.replace(" ", "").length == 16
                    && validityPeriod.text.length == 5
                    && cvcCode.length == 3
            Button(
                enabled = (step == 1 && selectedTable != null)
                        || (step == 2 && (isCardValid || (profile != null && profile.role == UserRole.ADMIN))),
                onClick = {
                    if (step == 1) {
                        step = 2
                    } else {
                        val card = if (profile == null || profile.role == UserRole.CLIENT) {
                            CardData(
                                cardNumber.text,
                                validityPeriod.text,
                                cvcCode.toIntOrNull() ?: 0
                            )
                        } else null
                        onSubmit(selectedTable!!, card)
                    }
                }
            ) {
                Text(if (step == 1) "Далее" else "Оформить")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                if (step == 1) onDismiss()
                else step = 1
            }) {
                Text(if (step == 1) "Отмена" else "Назад")
            }
        }
    )
}