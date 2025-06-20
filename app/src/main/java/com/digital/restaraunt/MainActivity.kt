package com.digital.restaraunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.digital.profile.presentation.ProfileViewModel
import com.digital.restaraunt.navigation.BottomBar
import com.digital.restaraunt.navigation.SetupNavGraph
import com.digital.restaraunt.ui.theme.RestarauntTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestarauntTheme {
                val navController = rememberNavController()
                val profileViewModel = koinInject<ProfileViewModel>()
                val user = profileViewModel.user.collectAsState()
                LaunchedEffect(profileViewModel) {
                    profileViewModel.fetchProfile()
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(
                            navController,
                            user.value
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        SetupNavGraph(navController)
                    }
                }
            }
        }
    }
}