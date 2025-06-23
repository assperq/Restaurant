package com.digital.restaraunt.di

import com.digital.order.data.OrderRepositoryImpl
import com.digital.order.domain.OrderRepository
import com.digital.order.presentation.OrderViewModel
import com.digital.payment.data.PaymentInterfaceImpl
import com.digital.payment.domain.PaymentInterface
import com.digital.profile.data.ProfileRepositoryImpl
import com.digital.profile.data.StatisticsRepositoryImpl
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.domain.StatisticsRepository
import com.digital.profile.presentation.ProfileViewModel
import com.digital.profile.presentation.StatisticsViewModel
import com.digital.registration.data.AuthRepositoryImpl
import com.digital.registration.domain.AuthRepository
import com.digital.registration.presentation.AuthViewModel
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
import java.lang.invoke.VarHandle.AccessMode.GET
import kotlin.math.sign
import kotlin.math.sin

val supabaseModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "URL",
            supabaseKey = "KEY"
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }
}

val reservationModule = module {
    single<ReservationRepository> { ReservationRepositoryImpl(get()) }
    viewModel<ReservationViewModel> { ReservationViewModel(get(), get()) }
}

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel<AuthViewModel> { AuthViewModel(get(), get()) }
}

val profileModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<StatisticsRepository> { StatisticsRepositoryImpl(get()) }
    single<ProfileViewModel> { ProfileViewModel(get()) }
    viewModel<StatisticsViewModel> { StatisticsViewModel(get()) }
}

val orderModule = module {
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    viewModel<OrderViewModel> { OrderViewModel(get(), get(), get(), get()) }
}

val paymentModule = module {
    single<PaymentInterface> { PaymentInterfaceImpl() }
}