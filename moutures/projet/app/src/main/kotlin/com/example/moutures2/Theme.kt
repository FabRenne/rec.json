package com.example.moutures2

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

// Chaque couleur de fond a sa couleur de texte « on… ».
private val LightColors = lightColorScheme(
    primary = Color(0xFF8D6E43),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF3E2C7),
    onPrimaryContainer = Color(0xFF2E1F08),
    secondaryContainer = Color(0xFFFBEBC4),
    onSecondaryContainer = Color(0xFF3E2C10),
    surfaceVariant = Color(0xFFEFE7DC),
    onSurfaceVariant = Color(0xFF4D4337),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFE3C08D),
    onPrimary = Color(0xFF3E2D12),
    primaryContainer = Color(0xFF5A4424),
    onPrimaryContainer = Color(0xFFF3E2C7),
    secondaryContainer = Color(0xFF5B4A1E),
    onSecondaryContainer = Color(0xFFFBEBC4),
    surfaceVariant = Color(0xFF4A4036),
    onSurfaceVariant = Color(0xFFD8CBBB),
)

@Composable
fun MouturesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,          // false = mes couleurs
    content: @Composable () -> Unit,
) {
    val colors = when {
        // Sans « dynamicColor && », Android 12+ remplacerait toujours vos couleurs.
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(colorScheme = colors, content = content)
}
