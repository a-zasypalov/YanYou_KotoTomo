package com.gaoyun.yanyou_kototomo.util

interface ThemeChanger {
    fun applyTheme()
    fun activateIcon(icon: AppIcon)
}

enum class AppIcon {
    Original, Clip, VerticalHalf
}

enum class AppTheme {
    Original, Green, Blue
}