package com.digital.reservations.data

import android.R.attr.x
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.text.toUpperCase
import com.digital.reservations.domain.ReservationRepository
import com.digital.reservations.domain.Table
import com.digital.reservations.domain.TableStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import java.time.temporal.TemporalQueries.zone
import java.util.Locale

class ReservationRepositoryImpl(
    supabaseClient : SupabaseClient
) : ReservationRepository {

    private val postgrest = supabaseClient.postgrest

    override suspend fun getTables(date: Instant): List<Table> {
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

    override suspend fun reserveTable(
        tableId: Int,
        userId: String,
        peopleCount: Int,
        date: Instant
    ): Boolean {
        return postgrest.rpc(
            function = "try_reserve_table",
            parameters = buildJsonObject {
                put("p_table_id", Json.encodeToJsonElement(tableId))
                put("p_user_id", Json.encodeToJsonElement(userId))
                put("p_reservation_date", Json.encodeToJsonElement(date))
                put("p_people_count", Json.encodeToJsonElement(peopleCount))
            }
        ).decodeSingle()
    }
}