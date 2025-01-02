package com.gaoyun.yanyou_kototomo.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Translate

internal fun settingsSections(
    primaryLanguage: String,
    onPrimaryLanguageChangeClick: () -> Unit,
    onAppIconClick: () -> Unit,
    onColorThemeClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onOnboardingClick: () -> Unit,
    onReloadCoursesClick: () -> Unit,
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
        title = "Primary Language",
        subtitle = primaryLanguage,
        icon = Icons.Default.Translate,
        onClick = onPrimaryLanguageChangeClick
    ),
    SettingsSection(
        title = "Show Onboarding",
        subtitle = "If you want to see the tutorial again",
        icon = Icons.Default.LocalLibrary,
        onClick = onOnboardingClick
    ),
    SettingsSection(
        title = "Reload courses",
        subtitle = "Force update courses",
        icon = Icons.Default.Refresh,
        onClick = onReloadCoursesClick
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