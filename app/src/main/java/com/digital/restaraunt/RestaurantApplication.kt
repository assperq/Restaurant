package com.digital.restaraunt

import android.app.Application
import com.digital.restaraunt.di.reservationModule
import com.digital.restaraunt.di.supabaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RestaurantApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                supabaseModule,
                reservationModule
            )
        }
    }
}