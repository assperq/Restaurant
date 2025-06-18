package com.digital.reservations.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digital.reservations.domain.Table

@Composable
fun TableLayout(
    tables: List<Table>,
    onTableClick: (Table) -> Unit,
    colorScheme: ColorScheme = MaterialTheme.colorScheme
) {
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            //.horizontalScroll(scrollStateX)
            .verticalScroll(scrollStateY)
    ) {
        Box(modifier = Modifier.size(750.dp)) {
            tables.forEach { table ->

                val tableSize = 60.dp
                val chairSize = 16.dp


                TableItem(
                    table = table,
                    tableSize = tableSize,
                    chairSize = chairSize,
                    onTableClick = onTableClick,
                    colorScheme = colorScheme
                )
            }
        }
    }
}

