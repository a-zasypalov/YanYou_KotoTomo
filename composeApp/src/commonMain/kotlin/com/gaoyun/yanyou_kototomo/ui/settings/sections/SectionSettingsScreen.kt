package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
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
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AutoResizeText(
                text = section.title(),
                fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.displayLarge.fontSize),
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
            )

            when (section) {
                SettingsSections.AppIcon -> {}
                SettingsSections.ColorTheme -> {}
                SettingsSections.AboutApp -> {}
            }
        }
    }
}

private fun SettingsSections.title() = when (this) {
    SettingsSections.AppIcon -> "App icon"
    SettingsSections.ColorTheme -> "Color theme"
    SettingsSections.AboutApp -> "About app"
}