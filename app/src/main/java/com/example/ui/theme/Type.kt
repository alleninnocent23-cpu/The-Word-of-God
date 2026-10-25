package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ReaderFont

val LoraFontFamily = FontFamily(
    Font(R.font.lora, FontWeight.Normal)
)

val PlayfairFontFamily = FontFamily(
    Font(R.font.playfair_display, FontWeight.Normal)
)

val OpenSansFontFamily = FontFamily(
    Font(R.font.open_sans, FontWeight.Normal)
)

val MonoFontFamily = FontFamily.Monospace
val SystemSerifFontFamily = FontFamily.Serif

fun getFontFamily(readerFont: ReaderFont): FontFamily {
    return when (readerFont) {
        ReaderFont.LORA -> LoraFontFamily
        ReaderFont.PLAYFAIR -> PlayfairFontFamily
        ReaderFont.OPEN_SANS -> OpenSansFontFamily
        ReaderFont.MONOSPACE -> MonoFontFamily
        ReaderFont.SYSTEM_SERIF -> SystemSerifFontFamily
    }
}

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlayfairFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PlayfairFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 23.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 21.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp
    )
)
