package com.digital.reservations.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerStateWithInitialDate()

    OutlinedButton(
        onClick = { openDialog.value = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.DateRange, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Выбрана дата: ${formatLocalDateRu(selectedDate)}")
    }

    // Диалог выбора даты
    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val pickedMillis = datePickerState.selectedDateMillis
                        if (pickedMillis != null) {
                            val pickedLocalDate = Instant.fromEpochMilliseconds(pickedMillis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                            onDateChange(pickedLocalDate)
                        }
                        openDialog.value = false
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberDatePickerStateWithInitialDate(): DatePickerState {
    val timeZone = TimeZone.currentSystemDefault()
    val tomorrow = Clock.System.todayIn(timeZone).plus(1, DateTimeUnit.DAY)
    val millisTomorrow = tomorrow.atStartOfDayIn(timeZone).toEpochMilliseconds()

    val validator = { dateMillis: Long ->
        dateMillis >= millisTomorrow
    }

    return rememberDatePickerState(
        initialSelectedDateMillis = millisTomorrow,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return validator(utcTimeMillis)
            }
        }
    )
}

fun formatLocalDateRu(date: LocalDate): String {
    val months = listOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    val monthName = months[date.monthNumber - 1]
    return "${date.dayOfMonth} $monthName ${date.year}"
}