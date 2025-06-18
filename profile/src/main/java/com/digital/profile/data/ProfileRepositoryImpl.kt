package com.digital.profile.data

import com.digital.profile.domain.Profile
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.domain.ReservationModel
import com.digital.profile.domain.ReservationStatus
import com.digital.profile.domain.UserRole
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement

private const val CACHE_DURATION_MS = 5 * 60 * 1000

class ProfileRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : ProfileRepository {
    private var cachedProfile: Profile? = null
    private var lastFetchTime: Long = 0

    override suspend fun getCurrentUser(): Profile? {
        val currentUser = supabaseClient.auth.currentUserOrNull() ?: return null

        if (cachedProfile != null &&
            System.currentTimeMillis() - lastFetchTime < CACHE_DURATION_MS) {
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
        lastFetchTime = System.currentTimeMillis()

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

    override fun clearCache() {
        cachedProfile = null
        lastFetchTime = 0
    }
}
