package com.gaoyun.yanyou_kototomo.data.ui_state

enum class ProgressColor(val maxDays: Int) {
    Red(1),      // Immediate recall (0-1 days)
    Orange(3),   // Short-term memory (2-3 days)
    Yellow(7),   // Basic retention (4-7 days)
    Green(14),   // Intermediate retention (8-14 days)
    Blue(Int.MAX_VALUE)  // Long-term retention (15+ days)
}