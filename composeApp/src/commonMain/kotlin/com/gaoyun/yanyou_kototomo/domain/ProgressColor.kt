package com.gaoyun.yanyou_kototomo.domain

import androidx.compose.ui.graphics.Color
import com.gaoyun.yanyou_kototomo.data.local.ProgressColor

fun mapIntervalToColor(intervalDays: Int): Color {
    return ProgressColor.entries.first { intervalDays <= it.maxDays }.toComposeColor()
}

fun ProgressColor.toComposeColor(): Color {
    return when (this) {
        ProgressColor.Red -> Color(0xFFE57373)
        ProgressColor.Orange -> Color(0xFFFFA726)
        ProgressColor.Yellow -> Color(0xFFFFD54F)
        ProgressColor.Green -> Color(0xFF81C784)
        ProgressColor.Blue -> Color(0xFF64B5F6)
    }
}