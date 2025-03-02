package com.gaoyun.yanyou_kototomo.ui.base.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    colorScheme: ColorScheme,
    content: @Composable() () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography(),
        content = content
    )
}

