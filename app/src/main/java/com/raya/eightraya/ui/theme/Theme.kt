package com.raya.eightraya.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val RayaBlue = Color(0xFF2557D6)
val RayaPurple = Color(0xFF7C3AED)
val RayaInk = Color(0xFF172033)
val RayaMuted = Color(0xFF65708A)
val RayaSurface = Color(0xFFFFFFFF)
val RayaBackground = Color(0xFFF6F7FB)
val RayaTrack = Color(0xFFE4E8F2)

private val RayaColorScheme = lightColorScheme(
    primary = RayaBlue,
    secondary = RayaPurple,
    background = RayaBackground,
    surface = RayaSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = RayaInk,
    onSurface = RayaInk,
)

@Composable
fun EightRayaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RayaColorScheme,
        content = content,
    )
}
