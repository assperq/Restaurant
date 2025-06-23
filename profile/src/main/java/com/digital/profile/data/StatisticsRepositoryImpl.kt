package com.digital.profile.data

import com.digital.profile.domain.StatisticsRepository
import com.digital.profile.domain.WaiterStats
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.slf4j.MDC.put

class StatisticsRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : StatisticsRepository {

    override suspend fun loadStatistics(date: LocalDate): List<WaiterStats> {
        val result = supabaseClient.postgrest.rpc(
            "get_waiter_stats_two_months",
            buildJsonObject {
                put("p_date", Json.encodeToJsonElement(date))
            }
        )

        val dtoList = result.decodeList<WaiterStatsDto>()
        return dtoList.map { it.toWaiterStats() }
    }
}