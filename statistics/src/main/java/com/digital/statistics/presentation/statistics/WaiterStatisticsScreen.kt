package com.digital.statistics.presentation.statistics

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.statistics.presentation.StatisticsViewModel
import com.digital.statistics.presentation.formatDate
import com.digital.statistics.presentation.statistics.WaiterStatisticsCard
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun WaiterStatsScreen(
    viewModel: StatisticsViewModel = koinViewModel()
) {
    var monthText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Статистика официантов",
            style = MaterialTheme.typography.titleSmall,
            fontSize = 22.sp
        )

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
                } catch (_: Exception) {
                    Toast.makeText(context, "Неверный формат даты", Toast.LENGTH_SHORT).show()
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
                        WaiterStatisticsCard(stat)
                    }
                }
            }
        }
    }
}