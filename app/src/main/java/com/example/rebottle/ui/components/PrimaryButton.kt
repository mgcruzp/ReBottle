package com.example.rebottle.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB8F8AD), // verde botón
            contentColor = Color(0xFF1B4332)    // texto verde oscuro
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}
