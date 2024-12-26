package com.gaoyun.yanyou_kototomo.ui.base.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class YanYouColorPair(val light: Color, val dark: Color)

data class YanYouColorScheme(
    val greenButtonContainer: Color,
    val blueButtonContainer: Color,
    val redButtonContainer: Color,
    val onButtonContainer: Color,
    val greenCorrect: Color = Color(0xFF70B657),
    val redWrong: Color = Color(0xFFB91A1A)
)

object ColorPairs {
    val greenButtonContainer: YanYouColorPair = YanYouColorPair(light = Color(0xFFb7e4c7), dark = Color(0xFFb7e4c7))
    val blueButtonContainer: YanYouColorPair = YanYouColorPair(light = Color(0xFFb5c7ff), dark = Color(0xFFb5c7ff))
    val redButtonContainer: YanYouColorPair = YanYouColorPair(light = Color(0xFFff926b), dark = Color(0xFFff926b))
    val onButtonContainer: YanYouColorPair = YanYouColorPair(light = Color(0xFF222222), dark = Color(0xFF222222))
}

@Composable
fun YanYouColorsProvider(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = YanYouColorScheme(
        greenButtonContainer = ColorPairs.greenButtonContainer.resolveColor(isDarkTheme),
        blueButtonContainer = ColorPairs.blueButtonContainer.resolveColor(isDarkTheme),
        redButtonContainer = ColorPairs.redButtonContainer.resolveColor(isDarkTheme),
        onButtonContainer = ColorPairs.onButtonContainer.resolveColor(isDarkTheme),
    )
    CompositionLocalProvider(YanYouColors provides colors) {
        content()
    }
}

val YanYouColors = staticCompositionLocalOf<YanYouColorScheme> {
    error("No ColorScheme provided")
}

private fun YanYouColorPair.resolveColor(isSystemInDarkMode: Boolean): Color {
    return if (isSystemInDarkMode) dark else light
}