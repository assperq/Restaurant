package com.digital.statistics.data


import com.digital.statistics.domain.DishStats
import com.digital.statistics.domain.StatisticsRepository
import com.digital.statistics.domain.WaiterStats
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement

class StatisticsRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : StatisticsRepository {

    override suspend fun loadWaiterStatistics(date: LocalDate): List<WaiterStats> {
        return supabaseClient.postgrest.rpc(
            "get_waiter_stats_two_months",
            buildJsonObject {
                put("p_date", Json.encodeToJsonElement(date))
            }
        ).decodeList<WaiterStatsDto>().map { it.toWaiterStats() }
    }

    override suspend fun loadDishesStatistics(date: LocalDate): List<DishStats> {
        return supabaseClient.postgrest.rpc(
            "get_dish_sales_two_months",
            buildJsonObject {
                put("p_date", Json.encodeToJsonElement(date))
            }
        ).decodeList<DishSalesStats>().map { it.toDishStat() }
    }
}

