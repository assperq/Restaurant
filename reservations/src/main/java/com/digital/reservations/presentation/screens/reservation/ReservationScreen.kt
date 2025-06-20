package com.digital.reservations.presentation.screens.reservation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digital.reservations.domain.Table
import com.digital.reservations.domain.TableStatus
import com.digital.reservations.presentation.ReservationViewModel
import com.digital.reservations.presentation.screens.DatePickerField
import com.digital.reservations.presentation.screens.ErrorDialog
import com.digital.reservations.presentation.screens.TableLayout
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

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
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var dialogText by remember {
        mutableStateOf("")
    }
    var dialogTitle by remember {
        mutableStateOf("")
    }

    if (showDialog) {
        ErrorDialog(
            message = dialogText,
            title = dialogTitle,
            onClickOk = {
                showDialog = false
                reservationViewModel.clearError()
            },
            onDismiss = {
                showDialog = false
                reservationViewModel.clearError()
            }
        )
    }

    var selectedDate : LocalDate by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(1, DateTimeUnit.DAY)
        )
    }

    LaunchedEffect(reservationViewModel) {
        reservationViewModel.fetchTables(selectedDate)
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
                    if (it.action != null && it.action == "reserve") {
                        dialogTitle = "Успешно"
                        dialogText = "Вы успешно забронировали стол"
                        showDialog = true
                    }
                }
                is ReservationViewModel.State.Error -> {
                    dialogTitle = "Ошибка бронирования"
                    dialogText = it.throwable.message?.take(100).toString()
                    showDialog = true
                }
            }
        }
    }

    var selectedTable : Table? by remember {
        mutableStateOf(null)
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            selectedTable?.let {
                TableDetailsView(
                    it
                ) { table, peopleCount ->
                    reservationViewModel.reserveTable(table.id, peopleCount, selectedDate)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    selectedTable = null
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DatePickerField(
                selectedDate = selectedDate,
                onDateChange = {
                    selectedDate = it
                    reservationViewModel.fetchTables(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    TableLayout(tables = tables.value, onTableClick = { table ->
                        if (table.status == TableStatus.FREE) {
                            selectedTable = table
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        }
                    })
                }
            }
        }
    }
}