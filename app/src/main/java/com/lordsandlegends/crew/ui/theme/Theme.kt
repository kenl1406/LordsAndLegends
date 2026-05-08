package com.lordsandlegends.crew.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/* ---------------- palette ---------------- */
object LLColors {
    val Ink = Color(0xFF1C2532)
    val Navy = Color(0xFF1F2A39)
    val Steel = Color(0xFF6F8B9E)
    val SteelSoft = Color(0xFFA8BCC8)
    val Copper = Color(0xFFD27A55)
    val CopperDeep = Color(0xFFAD5A3A)
    val CopperSoft = Color(0xFFECC6B3)
    val Parchment = Color(0xFFF3E9D6)
    val Parchment2 = Color(0xFFF7EFE0)
    val Bone = Color(0xFFFBF6EA)
    val Muted = Color(0xFF6F7C8C)
    val Line = Color(0x141C2532)        // ~8% ink
    val LineStrong = Color(0x2E1C2532)  // ~18%
    val Good = Color(0xFF2F7D5D)
    val Bad = Color(0xFFB1543C)
    val Surface = Color.White
}

/* ---------------- typography (system serif fallback for Cormorant) ---------------- */
@Suppress("UnusedReceiverParameter")
object LLType {
    val Serif: FontFamily = FontFamily.Serif
    val Sans: FontFamily = FontFamily.Default

    val Display = TextStyle(
        fontFamily = Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp,
    )
    val Headline = TextStyle(
        fontFamily = Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = (-0.4).sp,
    )
    val Title = TextStyle(
        fontFamily = Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = (-0.3).sp,
    )
    val SectionHeading = TextStyle(
        fontFamily = Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = (-0.2).sp,
    )
    val Body = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )
    val BodySmall = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.5f.sp,
        lineHeight = 18.sp,
    )
    val Eyebrow = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.6.sp,
    )
    val Italic = TextStyle(
        fontFamily = Serif,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}

private val LightScheme = lightColorScheme(
    primary = LLColors.Copper,
    onPrimary = Color.White,
    secondary = LLColors.Navy,
    onSecondary = Color.White,
    background = LLColors.Parchment2,
    onBackground = LLColors.Ink,
    surface = LLColors.Surface,
    onSurface = LLColors.Ink,
    surfaceVariant = LLColors.Parchment,
    onSurfaceVariant = LLColors.Muted,
    outline = LLColors.Line,
)

@Composable
fun LordsAndLegendsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Locked to the light "parchment" scheme for the demo — looks closer to the brand.
    MaterialTheme(
        colorScheme = LightScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
