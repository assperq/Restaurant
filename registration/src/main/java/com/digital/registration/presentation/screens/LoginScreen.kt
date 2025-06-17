package com.digital.registration.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.registration.presentation.AuthViewModel
import com.digital.registration.presentation.StringChecker
import org.koin.compose.viewmodel.koinViewModel
import com.digital.registration.R

@Composable
fun LoginScreen(
    onNavigateBack : () -> Unit,
    authViewModel: AuthViewModel = koinViewModel()
) {
    var emailText by remember {
        mutableStateOf("")
    }
    var passText by remember {
        mutableStateOf("")
    }

    Container {
        Column {
            AuthInputs(
                emailText = emailText,
                onEmailChange = {
                    emailText = it
                },
                passText = passText,
                onPasswordChange = {
                    passText = it
                },
                onNavigateBack = onNavigateBack
            )


            var checkedState by remember {
                mutableStateOf(false)
            }
            Row(
                modifier = Modifier
                    .height(28.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.width(250.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checkedState,
                        onCheckedChange = { checkedState = it },
                    )
                    BaseText(
                        "Запомнить меня",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                    )
                }
            }

            VerticalSpace(20.dp)

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                DefaultButton(
                    onClick = {
                        authViewModel.signIn(emailText, passText)
                    },
                    content = {
                        BaseText(
                            "Вход",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                )
            }
        }
    }
}