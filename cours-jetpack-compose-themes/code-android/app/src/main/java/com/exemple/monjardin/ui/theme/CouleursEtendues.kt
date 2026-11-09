package com.exemple.monjardin.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CouleursEtendues(
    val succes: Color, val surSucces: Color,
    val alerte: Color, val surAlerte: Color,
)

val CouleursClaires = CouleursEtendues(Color(0xFF1B6D2F), Color.White, Color(0xFF8A5100), Color.White)
val CouleursSombres = CouleursEtendues(Color(0xFF8BD89A), Color(0xFF00391A), Color(0xFFFFB870), Color(0xFF4A2800))

val LocalCouleursEtendues = staticCompositionLocalOf { CouleursClaires }

object JardinThemeExt {
    val couleurs: CouleursEtendues
        @Composable @ReadOnlyComposable
        get() = LocalCouleursEtendues.current
}
