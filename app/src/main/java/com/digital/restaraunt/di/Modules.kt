package com.digital.restaraunt.di

import com.digital.reservations.data.ReservationRepositoryImpl
import com.digital.reservations.domain.ReservationRepository
import com.digital.reservations.presentation.ReservationViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val supabaseModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "URL",
            supabaseKey = "KEY",
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }
}

val reservationModule = module {
    includes(supabaseModule)
    single<ReservationRepository> { ReservationRepositoryImpl(get()) }
    viewModel<ReservationViewModel> { ReservationViewModel(get()) }
}