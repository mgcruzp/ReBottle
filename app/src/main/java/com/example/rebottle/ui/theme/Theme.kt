package com.example.rebottle.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = DarkGreen,
    onPrimary = Color.White,
    secondary = Green1,
    onSecondary = Color.White,
    background = LightGreen,
    onBackground = Black,
    surface = Color.White,
    onSurface = Black
)

private val DarkColors = darkColorScheme(
    primary = MidGreen,
    onPrimary = Color.White,
    secondary = Green2,
    onSecondary = Color.White,
    background = Black,
    onBackground = Color.White,
    surface = DarkGreen,
    onSurface = Color.White
)

@Composable
fun RebottleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
