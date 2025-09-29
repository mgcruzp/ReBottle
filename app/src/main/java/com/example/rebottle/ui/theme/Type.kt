package com.example.rebottle.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

// Tipografía base Rebottle
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        color = Black
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 28.sp,
        color = DarkGreen // 👈 Titulares con el verde principal
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 22.sp,
        color = DarkGreen
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 18.sp,
        color = DarkGreen
    )
)
