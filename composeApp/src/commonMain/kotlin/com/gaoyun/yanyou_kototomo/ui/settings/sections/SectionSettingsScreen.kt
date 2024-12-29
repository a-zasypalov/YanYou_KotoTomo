package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSections
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun SectionSettingsScreen(
    section: SettingsSections,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = SettingsViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getSettings()
    }

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
    ) {
        when (section) {
            SettingsSections.AppIcon -> {}
            SettingsSections.ColorTheme -> {}
            SettingsSections.AboutApp -> {}
        }
    }
}