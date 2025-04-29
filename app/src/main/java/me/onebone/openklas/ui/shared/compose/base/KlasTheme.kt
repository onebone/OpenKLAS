package me.onebone.openklas.ui.shared.compose.base

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Copper,
    primaryVariant = CopperDark,
    secondary = Purple500,
    secondaryVariant = Purple700,
    background = DarkBackground,
    surface = DarkSurface,
    error = Red,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = White
)

private val LightColorPalette = lightColors(
    primary = Copper,
    primaryVariant = CopperDark,
    secondary = Purple500,
    secondaryVariant = Purple700,
    background = White,
    surface = UltraLightGray,
    error = Red,
    onPrimary = White,
    onSecondary = White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onError = White
)

@Composable
fun KlasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
