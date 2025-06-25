package com.digital.statistics.presentation.statistics

import android.widget.Toast
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
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DishStatsScreen(
    viewModel: StatisticsViewModel = koinViewModel()
) {
    var monthText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Статистика по блюдам",
            style = MaterialTheme.typography.titleLarge,
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
                    viewModel.fetchDishStatsForMonth(date)
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
            viewModel.dishStats.sortedBy { it.dishName }.forEach { stat ->
                DishStatisticsCard(stat)
            }
        }
    }
}
