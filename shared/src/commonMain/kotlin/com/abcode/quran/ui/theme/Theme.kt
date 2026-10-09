package com.abcode.quran.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Gold = Color(0xFFC5A059)
val GreenAccent = Color(0xFF5ABF90)
val DeepTeal = Color(0xFF0D3D3D)
val backgroundColor = Color(0xFF0F2626)
val SurfaceDark = Color(0xFF0A2A2A)

private val QuranDarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Color(0xFF1A1A1A),
    secondary = GreenAccent,
    onSecondary = Color(0xFF0A1A1A),
    background = backgroundColor,
    onBackground = Color(0xFFEDE7DA),
    surface = SurfaceDark,
    onSurface = Color(0xFFEDE7DA),
    surfaceVariant = DeepTeal,
    onSurfaceVariant = Color(0xFFC9C2B4),
)

@Composable
fun QuranTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuranDarkColorScheme,
        content = content,
    )
}
