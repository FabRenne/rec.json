package com.exemple.stockagecompose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Couleurs = lightColorScheme(
    primary = Color(0xFF3F51B5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE1FF),
    secondary = Color(0xFF00897B),
    background = Color(0xFFF7F7FB),
    surface = Color.White,
)

@Composable
fun StockageTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Couleurs, content = content)
}
