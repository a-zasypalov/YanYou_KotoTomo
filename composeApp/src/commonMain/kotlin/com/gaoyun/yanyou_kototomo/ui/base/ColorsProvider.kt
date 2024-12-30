package com.gaoyun.yanyou_kototomo.ui.base

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.ui.base.theme.BlueColors
import com.gaoyun.yanyou_kototomo.ui.base.theme.GreenColors
import com.gaoyun.yanyou_kototomo.ui.base.theme.OriginalColors
import com.gaoyun.yanyou_kototomo.util.AppTheme
import org.koin.core.component.KoinComponent

class ColorsProvider(private val preferences: Preferences) : KoinComponent {

    @Composable
    fun getCurrentScheme(): ColorScheme {
        val colorTheme = AppTheme.valueOf(preferences.getString(PreferencesKeys.COLOR_THEME, AppTheme.Original.name))
        val darkTheme = isSystemInDarkTheme()
        val colors = when (colorTheme) {
            AppTheme.Original -> if (darkTheme) OriginalColors.darkScheme else OriginalColors.lightScheme
            AppTheme.Blue -> if (darkTheme) BlueColors.darkScheme else BlueColors.lightScheme
            AppTheme.Green -> if (darkTheme) GreenColors.darkScheme else GreenColors.lightScheme
        }

        return colors
    }

}