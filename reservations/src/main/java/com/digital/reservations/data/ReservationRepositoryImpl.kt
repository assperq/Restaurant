package com.digital.reservations.data


import android.provider.SyncStateContract.Helpers.update
import com.digital.reservations.domain.ReservationRepository
import com.digital.reservations.domain.Table
import com.digital.reservations.domain.TableStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement

class ReservationRepositoryImpl(
    supabaseClient : SupabaseClient
) : ReservationRepository {

    private val postgrest = supabaseClient.postgrest

    override suspend fun getTables(date: LocalDate): List<Table> {
        return postgrest.rpc(
            function = "get_tables_with_virtual_status",
            parameters = buildJsonObject {
                put("target_date", Json.encodeToJsonElement(date))
            }
        ).decodeList<TableDto>().map {
            Table(
                id = it.id,
                waiterId = it.waiterId,
                status = TableStatus.valueOf(it.status.uppercase()),
                x = it.x,
                y = it.y
            )
        }
    }

    override suspend fun getTablesToday(): List<Table> {
        return postgrest.from("tables")
            .select()
            .decodeList<TodayTableDto>().map {
            Table(
                id = it.id,
                waiterId = it.waiterId,
                status = TableStatus.valueOf(it.status.uppercase()),
                x = it.x,
                y = it.y
            )
        }
    }

    override suspend fun updateTableStatus(
        tableId: Int,
        tableStatus: TableStatus
    ) {
        postgrest.from("tables").update(mapOf("status" to tableStatus.name.lowercase())) {
            filter {
                eq("id", tableId)
            }
        }
    }

    override suspend fun reserveTable(
        tableId: Int,
        userId: String,
        peopleCount: Int,
        date: LocalDate
    ): Boolean {

        val response = postgrest.rpc(
            function = "try_reserve_table",
            parameters = buildJsonObject {
                put("p_table_id", Json.encodeToJsonElement(tableId))
                put("p_user_id", Json.encodeToJsonElement(userId))
                put("p_reservation_date", Json.encodeToJsonElement(date))
                put("p_people_count", Json.encodeToJsonElement(peopleCount))
            }
        )
        return return Json.decodeFromString<Boolean>(response.data)
    }
}