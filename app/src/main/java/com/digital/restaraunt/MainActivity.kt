package com.digital.restaraunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.digital.reservations.presentation.ReservationViewModel
import com.digital.reservations.presentation.screens.ReservationScreen
import com.digital.restaraunt.ui.theme.RestarauntTheme
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestarauntTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    ReservationScreen()
                }
            }
        }
    }
}