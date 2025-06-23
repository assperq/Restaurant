package com.digital.profile.presentation.screens.statistics

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.digital.profile.presentation.StatisticsViewModel
import com.digital.profile.presentation.formatDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WaiterStatsScreen(
    viewModel: StatisticsViewModel = koinViewModel()
) {
    var monthText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Статистика официантов", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = monthText,
            onValueChange = { monthText = it },
            label = { Text("Месяц (формат YYYY-MM)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                try {
                    val (year, month) = monthText.split("-").map { it.toInt() }
                    val date = LocalDate(year, month, 1)
                    viewModel.fetchWaiterStatsForMonth(date)
                } catch (e: Exception) {
                    Toast.makeText(context, "Неверный формат даты: $e", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = monthText.matches(Regex("""\d{4}-\d{2}"""))
        ) {
            Text("Показать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val groupedStats = viewModel.waiterStats.groupBy { it.periodDate }

            groupedStats.forEach { (month, monthStats) ->
                Text(
                    text = formatDate(month),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    monthStats.sortedBy { it.fullName }.forEach { stat ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(stat.fullName, fontWeight = FontWeight.Bold)
                                Text("Принятых заказов: ${stat.ordersCount}")
                                Text("Оплаченных чеков: ${stat.paidChecksCount}")
                                Text("Сумма чеков: ${stat.paidChecksSum} ₽")

                                if (stat.ordersDiff != null || stat.checksDiff != null || stat.sumDiff != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val dynamic = if (stat.isImproving) "Положительная" else "Отрицательная"
                                    Text("Динамика: $dynamic")
                                    stat.ordersDiff?.let { Text("Изменение заказов: $it") }
                                    stat.checksDiff?.let { Text("Изменение чеков: $it") }
                                    stat.sumDiff?.let { Text("Изменение суммы: $it ₽") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}