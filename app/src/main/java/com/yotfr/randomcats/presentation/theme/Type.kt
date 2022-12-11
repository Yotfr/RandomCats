package com.yotfr.randomcats.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.yotfr.randomcats.R

private val Namu = FontFamily(
    fonts = listOf(
        Font(R.font.namu)
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Namu,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Namu,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Namu,
        fontSize = 36.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Namu,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Namu,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Namu,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Namu,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Namu,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Namu,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Namu,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Namu,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Namu,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Namu,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Namu,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Namu,
        fontSize = 11.sp
    )
)
