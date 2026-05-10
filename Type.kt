package com.example.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val KannadaFont = FontFamily.Default
val EnglishFont = FontFamily.Default

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = KannadaFont,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = KannadaFont,
        fontSize = 20.sp,
        lineHeight = 34.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = EnglishFont,
        fontSize = 16.sp
    )
)
