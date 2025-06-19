package com.digital.profile.data

import com.digital.profile.domain.DishEntry
import com.digital.profile.domain.OrderDetail
import com.digital.profile.domain.OrderStatus
import com.digital.profile.domain.Profile
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserOrder
import com.digital.profile.domain.UserRole
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDateTime.Companion
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.collections.first
import kotlin.collections.map

class ProfileRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : ProfileRepository {
    private var cachedProfile: Profile? = null

    override suspend fun getCurrentUser(): Profile? {
        val currentUser = supabaseClient.auth.currentUserOrNull() ?: return null

        if (cachedProfile != null) {
            return cachedProfile
        }

        val profileDto = supabaseClient.postgrest.from("profiles").select {
            filter { eq("id", currentUser.id) }
        }.decodeSingle<ProfileDto>()

        val profile = Profile(
            id = profileDto.id,
            email = profileDto.email,
            fullName = profileDto.fullName,
            role = UserRole.valueOf(profileDto.role.uppercase())
        )

        cachedProfile = profile

        return profile
    }

    override suspend fun getUserReservations(userId: String): List<ReservationModel> {
        return supabaseClient.postgrest.rpc(
            "get_user_reservations",
            buildJsonObject {
                put("target_user_id", Json.encodeToJsonElement(userId))
            }
        ).decodeList<ReservationDto>().map {
            ReservationModel(
                id = it.id,
                tableId = it.tableId,
                status = ReservationStatus.valueOf(it.status.uppercase()),
                reservationDate = it.reservationDate.toLocalDateTime(TimeZone.currentSystemDefault()),
                peopleCount = it.peopleCount
            )
        }
    }

    override suspend fun updateReservationStatus(
        reservationId: Int,
        status: ReservationStatus
    ) {
        supabaseClient.postgrest.rpc(
            "update_reservation_status",
            buildJsonObject {
                put("reservation_id", Json.encodeToJsonElement(reservationId))
                put("new_status", Json.encodeToJsonElement(status.name.lowercase()))
            }
        )
    }

    override suspend fun updateOrderStatus(
        orderId: Int,
        status: OrderStatus
    ) {
        supabaseClient.postgrest.from("orders").update(mapOf("status" to status.name.lowercase())) {
            filter {
                eq("id", orderId)
            }
        }
    }

    override suspend fun getUserOrders(userId: String) : List<UserOrder> {
        return supabaseClient.postgrest.rpc(
            "get_user_orders",
            buildJsonObject {
                put("p_user_id", Json.encodeToJsonElement(userId))
            }
        ).decodeList<OrderModelDto>().map {
            UserOrder(
                id = it.id,
                tableId = it.table_id,
                waiterId = it.waiter_id,
                userId = it.user_id.toString(),
                orderDate = it.order_date.toLocalDateTime(TimeZone.currentSystemDefault()),
                totalCost = it.total_cost.toLong(),
                status = OrderStatus.valueOf(it.status.uppercase())
            )
        }
    }

    override suspend fun getOrdersToday(): List<UserOrder> {
        return supabaseClient.postgrest.rpc(
            "get_today_orders",
        ).decodeList<OrderModelDto>().map {
            UserOrder(
                id = it.id,
                tableId = it.table_id,
                waiterId = it.waiter_id,
                userId = it.user_id.toString(),
                orderDate = it.order_date.toLocalDateTime(TimeZone.currentSystemDefault()),
                totalCost = it.total_cost.toLong(),
                status = OrderStatus.valueOf(it.status.uppercase())
            )
        }
    }

    override suspend fun getOrderDetails(orderId: Int): OrderDetail {
        val result = supabaseClient.postgrest.rpc(
            "get_order_details",
            buildJsonObject {
                put("p_order_id", Json.encodeToJsonElement(orderId))
            }
        ).decodeList<OrderDetailRow>()

        if (result.isEmpty()) throw IllegalStateException("Order not found")

        val first = result.first()

        return OrderDetail(
            orderId = first.orderId,
            orderDate = first.orderDate.toLocalDateTime(TimeZone.currentSystemDefault()),
            status = OrderStatus.valueOf(first.status.uppercase()),
            totalCost = first.totalCost.toLong(),
            waiterName = first.waiterName,
            dishes = result.map {
                DishEntry(
                    name = it.dishName,
                    quantity = it.quantity
                )
            }
        )
    }

    override fun clearCache() {
        cachedProfile = null
    }
}
