// Thème « Mon Jardin » — même code que la version Android (sauf dynamicColor, propre à Android 12+)
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Color.kt ----------
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

// ---------- Type.kt ----------
val Titres = FontFamily.Serif
val Corps = FontFamily.SansSerif

val JardinTypography = Typography(
    displayLarge = TextStyle(fontFamily = Titres, fontWeight = FontWeight.Bold, fontSize = 48.sp, lineHeight = 56.sp),
    headlineMedium = TextStyle(fontFamily = Titres, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
    titleLarge = TextStyle(fontFamily = Titres, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = Corps, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontFamily = Corps, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = Corps, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = Corps, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp),
)

// ---------- Shape.kt ----------
val JardinShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = CutCornerShape(16.dp),
)

// ---------- Couleurs étendues (CompositionLocal) ----------
@Immutable
data class CouleursEtendues(val succes: Color, val surSucces: Color, val alerte: Color, val surAlerte: Color)

val CouleursClaires = CouleursEtendues(Color(0xFF1B6D2F), Color.White, Color(0xFF8A5100), Color.White)
val CouleursSombres = CouleursEtendues(Color(0xFF8BD89A), Color(0xFF00391A), Color(0xFFFFB870), Color(0xFF4A2800))

val LocalCouleursEtendues = staticCompositionLocalOf { CouleursClaires }

// ---------- Theme.kt ----------
@Composable
fun JardinTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val etendues = if (darkTheme) CouleursSombres else CouleursClaires
    CompositionLocalProvider(LocalCouleursEtendues provides etendues) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = JardinTypography,
            shapes = JardinShapes,
            content = content,
        )
    }
}

object JardinThemeExt {
    val couleurs: CouleursEtendues
        @Composable @ReadOnlyComposable get() = LocalCouleursEtendues.current
}
