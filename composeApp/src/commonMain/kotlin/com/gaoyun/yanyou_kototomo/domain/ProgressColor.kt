package com.gaoyun.yanyou_kototomo.domain

import androidx.compose.ui.graphics.Color
import com.gaoyun.yanyou_kototomo.data.local.ProgressColor

fun mapIntervalToColor(intervalDays: Int): Color {
    return ProgressColor.entries.first { intervalDays <= it.maxDays }.toComposeColor()
}

fun ProgressColor.toComposeColor(): Color {
    return when (this) {
        ProgressColor.Red -> Color(0xFFE57373)    // Light Red (Material Red 300)
        ProgressColor.Orange -> Color(0xFFFFB74D) // Light Orange (Material Orange 300)
        ProgressColor.Yellow -> Color(0xFFFFD54F) // Light Yellow (Material Yellow 300)
        ProgressColor.Green -> Color(0xFF81C784)  // Light Green (Material Green 300)
        ProgressColor.Blue -> Color(0xFF64B5F6)   // Light Blue (Material Blue 300)
    }
}