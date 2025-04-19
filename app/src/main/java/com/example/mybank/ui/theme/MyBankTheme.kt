package com.example.mybank.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


object BankColors {
    val primaryColor = Color(0xFF1E4A9E) // Bleu foncé bancaire
    val accentColor = Color(0xFF3D7AE6)  // Bleu plus clair pour les boutons
    val lightBlue = Color(0xFFD0E1FF)    // Bleu très clair pour les accents
    val backgroundColor = Color(0xFFF5F7FA) // Gris très clair
}

private val LightColorScheme = lightColorScheme(
    primary = BankColors.primaryColor,
    onPrimary = Color.White,
    secondary = BankColors.accentColor,
    background = BankColors.backgroundColor,
    surface = Color.White,
    surfaceVariant = BankColors.lightBlue.copy(alpha = 0.3f),
    onSurface = Color.Black,
    onSurfaceVariant = Color.Gray,
    outline = Color.LightGray,
    outlineVariant = Color.LightGray.copy(alpha = 0.5f)
)

private val DarkColorScheme = darkColorScheme(
    primary = BankColors.accentColor,
    onPrimary = Color.White,
    secondary = BankColors.lightBlue,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF252525),
    onSurface = Color.White,
    onSurfaceVariant = Color.LightGray,
    outline = Color.DarkGray,
    outlineVariant = Color.DarkGray.copy(alpha = 0.5f)
)

@Composable
fun MyBankTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}