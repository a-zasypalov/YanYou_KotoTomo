package com.gaoyun.yanyou_kototomo.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RestartAlt

internal fun settingsSections(
    onAppIconClick: () -> Unit,
    onColorThemeClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onResetClick: () -> Unit,
) = listOf(
    SettingsSection(
        title = "App Icon",
        subtitle = "Change the app icon",
        icon = Icons.Default.Photo,
        onClick = onAppIconClick
    ),
    SettingsSection(
        title = "Color Theme",
        subtitle = "Customize your theme",
        icon = Icons.Default.Palette,
        onClick = onColorThemeClick
    ),
    SettingsSection(
        title = "About App",
        subtitle = "Learn more about this app",
        icon = Icons.Default.Info,
        onClick = onAboutAppClick
    ),
    SettingsSection(
        title = "Reset",
        subtitle = "Reset all settings to default",
        icon = Icons.Default.RestartAlt,
        onClick = onResetClick,
        isDestructive = true
    )
)