package com.exemple.monjardin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Pour une police personnalisée : placez le fichier .ttf dans res/font/
// puis : val Titres = FontFamily(Font(R.font.ma_police, FontWeight.Bold))
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
