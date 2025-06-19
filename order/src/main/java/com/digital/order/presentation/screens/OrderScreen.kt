package com.digital.order.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.digital.order.presentation.OrderViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderScreen(
    viewModel: OrderViewModel = koinViewModel(),
    onOrderSuccess: () -> Unit
) {
    val orderResult by remember {
        mutableStateOf(viewModel.orderResult)
    }

    LaunchedEffect(orderResult) {
        if (orderResult.value?.success == true) {
            onOrderSuccess()
        }
    }

    Scaffold(
        bottomBar = {
            CartBottomBar(
                itemCount = viewModel.cart.values.sum(),
                totalPrice = viewModel.cart.entries.sumOf { it.key.price * it.value },
                onCheckoutClick = { viewModel.showTableDialog() },
                enabled = viewModel.cart.isNotEmpty()
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (viewModel.isLoading && viewModel.dishes.isEmpty()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(viewModel.dishes) { dish ->
                        DishItem(
                            dish = dish,
                            count = viewModel.cart[dish] ?: 0,
                            onAdd = { viewModel.addToCart(dish) },
                            onRemove = { viewModel.removeFromCart(dish) }
                        )
                    }
                }
            }
        }
    }

    val scope = rememberCoroutineScope()

    if (viewModel.isTableDialogVisible.value) {
        TableSelectionDialog(
            tables = viewModel.tables,
            selectedTable = viewModel.selectedTable.value,
            onTableSelected = { viewModel.selectTable(it) },
            onDismiss = { viewModel.hideTableDialog() },
            onConfirm = {
                viewModel.hideTableDialog()
                scope.launch {
                    viewModel.makeOrder()
                }
            },
            confirmEnabled = viewModel.selectedTable.value != null
        )
    }

    orderResult.value?.let { result ->
        OrderResultDialog(
            result = result,
            dishes = viewModel.dishes,
            onDismiss = { viewModel.clearOrderResult() }
        )
    }
}
