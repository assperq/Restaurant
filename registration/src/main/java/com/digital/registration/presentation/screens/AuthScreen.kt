package com.digital.registration.presentation.screens

import android.R.attr.onClick
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainAuthScreen(
    onLoginScreenNavigate : () -> Unit,
    onRegistrationScreenNavigate : () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column {
            DefaultButton(
                onClick = onLoginScreenNavigate,
            ) { Text("Войти") }
            DefaultButton(
                onClick = onRegistrationScreenNavigate,
            ) { Text("Зарегистрироваться") }
        }
    }
}