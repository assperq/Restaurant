package com.digital.order.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartBottomBar(
    itemCount: Int,
    totalPrice: Double,
    onCheckoutClick: () -> Unit,
    enabled: Boolean
) {
    Surface(
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("$itemCount товаров")
                Text("$totalPrice ₽")
            }

            Button(
                onClick = onCheckoutClick,
                enabled = enabled
            ) {
                Text("Оформить заказ")
            }
        }
    }
}