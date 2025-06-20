package com.digital.order.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.digital.order.presentation.OrderViewModel
import com.digital.profile.domain.Profile
import com.digital.profile.domain.UserRole
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderScreen(
    profile : Profile?,
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

    if (viewModel.isTableDialogVisible.value) {
        OrderPlacementDialog(
            profile = profile,
            tables = viewModel.tables,
            onDismiss = { viewModel.hideTableDialog() },
            onSubmit = { table, cardData ->
                viewModel.selectedTable.value = table
                if (cardData == null) {
                    viewModel.makeOrderWithTerminal()
                }
                else {
                    viewModel.makeOrder(cardData)
                }
                viewModel.hideTableDialog()
            }
        )
    }

    if (viewModel.error.value != null) {
        ErrorDialog(
            message = viewModel.error.value!!.message.toString(),
            title = "Ошибка заказа",
            onClickOk = {
                viewModel.clearError()
            }
        ) {}
    }

    if (viewModel.isLoading.value) {
        Dialog(
            onDismissRequest = {}
        ) {
            Card(modifier = Modifier.size(70.dp).clip(RoundedCornerShape(14.dp))) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    orderResult.value?.let { result ->
        OrderResultDialog(
            result = result,
            dishes = viewModel.dishes,
            onDismiss = { viewModel.clearOrderResult() }
        )
    }
}
