package com.digital.restaraunt.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    // Основные цвета
    primary = Color(0xFF8B0000),       // Бордовый (Primary)
    secondary = Color(0xFFDAA520),     // Золотистый (Secondary)
    tertiary = Color(0xFFA52A2A),      // Мягкий бордовый (Tertiary, доп. акцент)

    // Фон и поверхности
    background = Color(0xFFFFF9F5),    // Тёплый белый (Background)
    surface = Color(0xFFFFFFFF),       // Чистый белый (Surface для карточек)
    onPrimary = Color(0xFFFFFFFF),    // Текст/иконки на бордовом
    onSecondary = Color(0xFF000000),  // Текст на золотистом (чёрный для контраста)
    onTertiary = Color(0xFFFFFFFF),   // Текст на tertiary (белый)
    onBackground = Color(0xFF333333),  // Основной текст (тёмно-серый)
    onSurface = Color(0xFF1C1B1F),     // Вторичный текст (стандартный Material-3)

    // Дополнительные параметры (опционально)
    error = Color(0xFFB22222),         // Красный для ошибок
    errorContainer = Color(0xFFFFDAD6), // Светлый фон ошибок
    onError = Color(0xFFFFFFFF),       // Текст на красном
    outline = Color(0xFFE0E0E0),       // Границы (разделители)
    surfaceVariant = Color(0xFFF5F5F5) // Альтернативный фон (например, для полей ввода)
)

@Composable
fun RestarauntTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}