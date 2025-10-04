package com.example.rebottle.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fontSize: TextUnit = 22.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB8F8AD),
            contentColor = Color(0xFF1B4332)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = text,
            fontSize = fontSize
        )
    }
}
