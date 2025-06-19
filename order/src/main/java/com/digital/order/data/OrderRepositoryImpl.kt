package com.digital.order.data

import com.digital.order.domain.DishModel
import com.digital.order.domain.DishOrderModel
import com.digital.order.domain.MissingItem
import com.digital.order.domain.OrderRepository
import com.digital.order.domain.OrderResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement

class OrderRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : OrderRepository {
    override suspend fun makeOrder(
        tableId: Int,
        userId: String,
        dishes: List<DishOrderModel>
    ) : OrderResult {
        return supabaseClient.postgrest.rpc(
            "make_food_order",
            buildJsonObject {
                put("p_table_id", Json.encodeToJsonElement(tableId))
                put("p_user_id", Json.encodeToJsonElement(userId))
                put("p_dishes", Json.encodeToJsonElement(dishes.map { DishOrderDto(it.dishId, it.quantity) }))
            }
        ).decodeAs<OrderResultDto>().let {
            OrderResult(
                success = it.success,
                orderId = it.order_id,
                error = it.error,
                missing = it.missing?.map {
                    MissingItem(
                        dishId = it.dish_id,
                        required = it.required,
                        available = it.available
                    )
                }
            )
        }
    }

    override suspend fun getDishesList(): List<DishModel> {
        return supabaseClient.postgrest.from("dishes").select().decodeList<DishDto>().map {
            DishModel(
                id = it.id,
                name = it.name,
                price = it.price,
                availableQuantity = it.availableQuantity,
                imageUrl = it.imageUrl
            )
        }
    }

}