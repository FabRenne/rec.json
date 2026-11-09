package com.exemple.monjardin.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Vert40 = Color(0xFF2E6B30)
val Vert80 = Color(0xFF96D68F)
val VertClair = Color(0xFFB1F2A9)

val LightColors = lightColorScheme(
    primary = Vert40, onPrimary = Color.White,
    primaryContainer = VertClair, onPrimaryContainer = Color(0xFF002106),
    secondary = Color(0xFF52634F), onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5E8CF), onSecondaryContainer = Color(0xFF101F10),
    tertiary = Color(0xFF38656A), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBCEBF0), onTertiaryContainer = Color(0xFF002023),
    error = Color(0xFFBA1A1A), onError = Color.White,
    errorContainer = Color(0xFFFFDAD6), onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF7FBF1), onBackground = Color(0xFF181D17),
    surface = Color(0xFFF7FBF1), onSurface = Color(0xFF181D17),
    surfaceVariant = Color(0xFFDEE5D8), onSurfaceVariant = Color(0xFF424940),
    outline = Color(0xFF72796F),
)

val DarkColors = darkColorScheme(
    primary = Vert80, onPrimary = Color(0xFF00390E),
    primaryContainer = Color(0xFF13521A), onPrimaryContainer = VertClair,
    secondary = Color(0xFFB9CCB4), onSecondary = Color(0xFF253423),
    secondaryContainer = Color(0xFF3B4B38), onSecondaryContainer = Color(0xFFD5E8CF),
    tertiary = Color(0xFFA0CFD4), onTertiary = Color(0xFF00363B),
    tertiaryContainer = Color(0xFF1F4D52), onTertiaryContainer = Color(0xFFBCEBF0),
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A), onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF10140F), onBackground = Color(0xFFE0E4DA),
    surface = Color(0xFF10140F), onSurface = Color(0xFFE0E4DA),
    surfaceVariant = Color(0xFF424940), onSurfaceVariant = Color(0xFFC2C9BD),
    outline = Color(0xFF8C9388),
)
