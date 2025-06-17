package com.digital.reservations.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.digital.reservations.domain.Table
import com.digital.reservations.presentation.ReservationViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import java.util.Calendar

@Composable
fun ReservationScreen(
    reservationViewModel: ReservationViewModel = koinViewModel()
) {
    val tables = remember {
        mutableStateOf<List<Table>>(emptyList())
    }
    val isLoading = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(reservationViewModel) {
        reservationViewModel.state.collect {
            when(it) {
                ReservationViewModel.State.Default -> {
                    isLoading.value = false
                }
                ReservationViewModel.State.Loading -> {
                    isLoading.value = true
                }
                is ReservationViewModel.State.Success -> {
                    isLoading.value = false
                    tables.value = it.list
                }
            }
        }
    }

    var selectedDate : LocalDate by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(1, DateTimeUnit.DAY)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DatePickerField(
            selectedDate = selectedDate,
            onDateChange = {
                selectedDate = it
                reservationViewModel.fetchTables(it.atStartOfDayIn(TimeZone.currentSystemDefault()))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                TableLayout(tables = tables.value, onTableClick = { table ->
                    // обработка нажатия на стол
                    // например, открой диалог ввода peopleCount и вызови:
                    // viewModel.reserveTable(...)
                })
            }
        }
    }
}