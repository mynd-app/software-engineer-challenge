package com.example.myndchallenge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Navy = Color(0xFF1B1F5E)
private val Apricot = Color(0xFFF4B172)
private val Cream = Color(0xFFFDF8F2)
private val Sand = Color(0xFFF2E6D8)

private val colors =
    lightColorScheme(
        primary = Navy,
        onPrimary = Color.White,
        secondary = Apricot,
        onSecondary = Navy,
        secondaryContainer = Sand,
        onSecondaryContainer = Navy,
        background = Cream,
        surface = Cream,
        surfaceVariant = Sand,
    )

@Composable
fun MyndTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, content = content)
}
