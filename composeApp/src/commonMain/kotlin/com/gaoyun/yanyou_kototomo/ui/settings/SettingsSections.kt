package com.gaoyun.yanyou_kototomo.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.settings_section_about_app_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_about_app_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_app_icon_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_app_icon_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_color_theme_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_color_theme_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_primary_language_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_reload_courses_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_reload_courses_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_reset_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_reset_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_show_onboarding_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_show_onboarding_title
import yanyou_kototomo.composeapp.generated.resources.settings_section_spacial_repetition_subtitle
import yanyou_kototomo.composeapp.generated.resources.settings_section_spacial_repetition_title

data class SettingsSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false,
)

@Composable
internal fun settingsSections(
    primaryLanguage: String,
    onPrimaryLanguageChangeClick: () -> Unit,
    onAppIconClick: () -> Unit,
    onColorThemeClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onOnboardingClick: () -> Unit,
    onReloadCoursesClick: () -> Unit,
    onResetClick: () -> Unit,
    onSpacialRepetitionSettingsClick: () -> Unit,
) = listOf(
    SettingsSection(
        id = "app_icon",
        title = stringResource(Res.string.settings_section_app_icon_title),
        subtitle = stringResource(Res.string.settings_section_app_icon_subtitle),
        icon = Icons.Default.Photo,
        onClick = onAppIconClick
    ),
    SettingsSection(
        id = "color_theme",
        title = stringResource(Res.string.settings_section_color_theme_title),
        subtitle = stringResource(Res.string.settings_section_color_theme_subtitle),
        icon = Icons.Default.Palette,
        onClick = onColorThemeClick
    ),
    SettingsSection(
        id = "primary_language",
        title = stringResource(Res.string.settings_section_primary_language_title),
        subtitle = primaryLanguage,
        icon = Icons.Default.Translate,
        onClick = onPrimaryLanguageChangeClick
    ),
    SettingsSection(
        id = "spaced_repetition",
        title = stringResource(Res.string.settings_section_spacial_repetition_title),
        subtitle = stringResource(Res.string.settings_section_spacial_repetition_subtitle),
        icon = Icons.Default.Repeat,
        onClick = onSpacialRepetitionSettingsClick
    ),
    SettingsSection(
        id = "show_onboarding",
        title = stringResource(Res.string.settings_section_show_onboarding_title),
        subtitle = stringResource(Res.string.settings_section_show_onboarding_subtitle),
        icon = Icons.Default.LocalLibrary,
        onClick = onOnboardingClick
    ),
    SettingsSection(
        id = "reload_courses",
        title = stringResource(Res.string.settings_section_reload_courses_title),
        subtitle = stringResource(Res.string.settings_section_reload_courses_subtitle),
        icon = Icons.Default.Refresh,
        onClick = onReloadCoursesClick
    ),
    SettingsSection(
        id = "about_app",
        title = stringResource(Res.string.settings_section_about_app_title),
        subtitle = stringResource(Res.string.settings_section_about_app_subtitle),
        icon = Icons.Default.Info,
        onClick = onAboutAppClick
    ),
    SettingsSection(
        id = "reset",
        title = stringResource(Res.string.settings_section_reset_title),
        subtitle = stringResource(Res.string.settings_section_reset_subtitle),
        icon = Icons.Default.RestartAlt,
        onClick = onResetClick,
        isDestructive = true
    )
)