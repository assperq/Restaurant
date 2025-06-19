package com.digital.order.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.digital.order.domain.DishModel
import com.digital.order.domain.OrderResult

@Composable
fun OrderResultDialog(
    result: OrderResult,
    dishes : List<DishModel>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (result.success) "Заказ оформлен" else "Ошибка")
        },
        text = {
            Column {
                if (result.success) {
                    Text("Номер заказа: ${result.orderId}")
                } else if (!result.error.isNullOrEmpty()) {
                    Text(result.error)
                } else if (!result.missing.isNullOrEmpty()) {
                    Text("Недостаточно ингредиентов для:")
                    result.missing.forEach { item ->
                        val dish = dishes.find { it.id == item.dishId }
                        Text("- Блюдо \"${dish?.name ?: item.dishId}\": нужно ${item.required}, доступно ${item.available}")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}