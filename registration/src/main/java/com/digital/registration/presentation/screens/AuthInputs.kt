package com.digital.registration.presentation.screens

import android.R.attr.singleLine
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.registration.R

@Composable
fun AuthInputs(
    emailText : String,
    onEmailChange : (String) -> Unit,
    passText : String,
    onPasswordChange : (String) -> Unit,
    onNavigateBack : () -> Unit
) {
    var passVisibility by remember {
        mutableStateOf(false)
    }
    Column {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                null)
        }
        VerticalSpace()
        BaseText("Email", fontSize = 14.sp)
        OutlinedTextField(
            value = emailText,
            onValueChange = onEmailChange,
            placeholder = {
                BaseText("@example.com", fontSize = 14.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        VerticalSpace()
        BaseText("Пароль", fontSize = 14.sp)
        OutlinedTextField(
            value = passText,
            onValueChange = onPasswordChange,
            placeholder = {
                Text("*****************", fontSize = 14.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passVisibility = !passVisibility }) {
                    Icon(
                        painter = painterResource(if (passVisibility) R.drawable.lock_ic else R.drawable.ic_lock_open),
                        contentDescription = if (passVisibility) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            singleLine = true
        )

        VerticalSpace(10.dp)
    }
}