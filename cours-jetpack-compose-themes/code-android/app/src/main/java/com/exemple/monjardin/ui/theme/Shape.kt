package com.exemple.monjardin.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val JardinShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = CutCornerShape(16.dp),
)
