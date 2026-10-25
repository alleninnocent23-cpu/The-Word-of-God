package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.AppThemeMode

private val ObsidianColorScheme = darkColorScheme(
    primary = ObsidianGold,
    onPrimary = Color.Black,
    primaryContainer = ObsidianCard,
    onPrimaryContainer = ObsidianGoldLight,
    secondary = ObsidianGoldLight,
    onSecondary = Color.Black,
    background = ObsidianBackground,
    onBackground = ObsidianTextPrimary,
    surface = ObsidianSurface,
    onSurface = ObsidianTextPrimary,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = ObsidianTextSecondary,
    outline = ObsidianCardBorder
)

private val SepiaColorScheme = lightColorScheme(
    primary = SepiaBrown,
    onPrimary = Color.White,
    primaryContainer = SepiaCard,
    onPrimaryContainer = SepiaTextPrimary,
    secondary = SepiaBrown,
    onSecondary = Color.White,
    background = SepiaBackground,
    onBackground = SepiaTextPrimary,
    surface = SepiaSurface,
    onSurface = SepiaTextPrimary,
    surfaceVariant = SepiaCard,
    onSurfaceVariant = SepiaTextSecondary,
    outline = SepiaCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightCard,
    onPrimaryContainer = LightTextPrimary,
    secondary = LightPrimary,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightCardBorder
)

private val NavyColorScheme = darkColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.Black,
    primaryContainer = NavyCard,
    onPrimaryContainer = NavyTextPrimary,
    secondary = NavyPrimary,
    onSecondary = Color.Black,
    background = NavyBackground,
    onBackground = NavyTextPrimary,
    surface = NavySurface,
    onSurface = NavyTextPrimary,
    surfaceVariant = NavyCard,
    onSurfaceVariant = NavyTextSecondary,
    outline = NavyCardBorder
)

private val ForestColorScheme = darkColorScheme(
    primary = ForestPrimary,
    onPrimary = Color.Black,
    primaryContainer = ForestCard,
    onPrimaryContainer = ForestTextPrimary,
    secondary = ForestPrimary,
    onSecondary = Color.Black,
    background = ForestBackground,
    onBackground = ForestTextPrimary,
    surface = ForestSurface,
    onSurface = ForestTextPrimary,
    surfaceVariant = ForestCard,
    onSurfaceVariant = ForestTextSecondary,
    outline = ForestCardBorder
)

fun getColorSchemeForTheme(themeMode: AppThemeMode): ColorScheme {
    return when (themeMode) {
        AppThemeMode.OBSIDIAN_DARK -> ObsidianColorScheme
        AppThemeMode.SEPIA_PARCHMENT -> SepiaColorScheme
        AppThemeMode.CRISP_LIGHT -> LightColorScheme
        AppThemeMode.MIDNIGHT_NAVY -> NavyColorScheme
        AppThemeMode.OLIVE_FOREST -> ForestColorScheme
    }
}

@Composable
fun ScriptureBibleTheme(
    themeMode: AppThemeMode = AppThemeMode.OBSIDIAN_DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = getColorSchemeForTheme(themeMode)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
