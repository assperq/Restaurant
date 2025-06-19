package com.digital.order.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.order.domain.DishModel
import com.digital.order.domain.DishOrderModel
import com.digital.order.domain.OrderRepository
import com.digital.order.domain.OrderResult
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.presentation.ProfileViewModel
import com.digital.reservations.domain.ReservationRepository
import com.digital.reservations.domain.Table
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.KoinApplication.Companion.init

class OrderViewModel(
    private val repository: OrderRepository,
    private val reservationRepository: ReservationRepository,
    private val profileViewModel: ProfileViewModel
) : ViewModel() {
    private val _dishes = mutableStateListOf<DishModel>()
    val dishes: List<DishModel> = _dishes

    private val _cart = mutableStateMapOf<DishModel, Int>()
    val cart: Map<DishModel, Int> = _cart

    val selectedTable = mutableStateOf<Table?>(null)

    val isTableDialogVisible = mutableStateOf(false)

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean = _isLoading.value

    val orderResult = mutableStateOf<OrderResult?>(null)

    var tables: List<Table> = emptyList()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _dishes.addAll(repository.getDishesList())
                tables = reservationRepository
                    .getTables(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
            } catch (e: Exception) {
                Log.d("LOG", e.message.toString())
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(dish: DishModel) {
        _cart[dish] = (_cart[dish] ?: 0) + 1
    }

    fun removeFromCart(dish: DishModel) {
        val current = _cart[dish] ?: return
        if (current <= 1) {
            _cart.remove(dish)
        } else {
            _cart[dish] = current - 1
        }
    }

    fun selectTable(table: Table) {
        selectedTable.value = table
    }

    fun showTableDialog() {
        isTableDialogVisible.value = true
    }

    fun hideTableDialog() {
        isTableDialogVisible.value = false
    }

    fun clearOrderResult() {
        orderResult.value = null
    }

    suspend fun makeOrder(): Boolean {
        if (selectedTable.value == null) return false

        val userId = profileViewModel.user.value?.id ?: return false
        val dishes = _cart.map {
            DishOrderModel(it.key.id, it.value)
        }

        _isLoading.value = true
        return try {
            val result = repository.makeOrder(
                tableId = selectedTable.value!!.id,
                userId = userId,
                dishes = dishes
            )
            orderResult.value = result
            if (result.success) {
                _cart.clear()
                selectedTable.value = null
            }
            result.success
        } catch (e: Exception) {
            Log.d("LOG", e.message.toString())
            false
        } finally {
            _isLoading.value = false
        }
    }
}