package com.gaoyun.yanyou_kototomo.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.domain.toStringRes
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSections
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToOnboarding
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToSettingsSection
import com.gaoyun.yanyou_kototomo.ui.settings.dialogs.CoursesReloadDialog
import com.gaoyun.yanyou_kototomo.ui.settings.dialogs.PrimaryLanguageChooser
import com.gaoyun.yanyou_kototomo.ui.settings.dialogs.ResetDialog
import com.gaoyun.yanyou_kototomo.ui.settings.sections.SettingsViewModel
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = SettingsViewModel::class)

    var showResetDialog = remember { mutableStateOf(false) }
    val showReloadConfirmation = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getSettings()
    }

    val showPrimaryLanguageChooser = remember { mutableStateOf(false) }
    val onPrimaryLanguageChangeClick = {
        showPrimaryLanguageChooser.value = true
    }


    viewModel.viewState.collectAsState().value?.let { viewState ->
        val primaryLanguage = stringResource(viewState.primaryLanguageId.toStringRes())
        val selectedLanguage = remember { mutableStateOf(primaryLanguage) }

        SettingsScreenContent(
            modifier = modifier,
            primaryLanguage = stringResource(viewState.primaryLanguageId.toStringRes()),
            onAppIconClick = { navigate(ToSettingsSection(SettingsSections.AppIcon)) },
            onColorThemeClick = { navigate(ToSettingsSection(SettingsSections.ColorTheme)) },
            onAboutAppClick = { navigate(ToSettingsSection(SettingsSections.AboutApp)) },
            onOnboardingClick = { navigate(ToOnboarding) },
            onPrimaryLanguageChange = onPrimaryLanguageChangeClick,
            onReloadCoursesClick = { showReloadConfirmation.value = true },
            onResetClick = { showResetDialog.value = true },
        )

        PrimaryLanguageChooser(
            showDialog = showPrimaryLanguageChooser,
            availableLanguages = viewState.availableLanguages.map { it to stringResource(it.toStringRes()) },
            selectedLanguage = selectedLanguage,
            onPrimaryLanguageChange = viewModel::setPrimaryLanguageId,
        )
    }


    CoursesReloadDialog(showReloadConfirmation, viewModel::reloadCourses)
    ResetDialog(showResetDialog, viewModel::resetAllData)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    modifier: Modifier,
    primaryLanguage: String,
    onAppIconClick: () -> Unit,
    onColorThemeClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onOnboardingClick: () -> Unit,
    onPrimaryLanguageChange: () -> Unit,
    onReloadCoursesClick: () -> Unit,
    onResetClick: () -> Unit,
) {

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            )
        }

        items(
            settingsSections(
                primaryLanguage,
                onPrimaryLanguageChange,
                onAppIconClick,
                onColorThemeClick,
                onAboutAppClick,
                onOnboardingClick,
                onReloadCoursesClick,
                onResetClick
            )
        ) { section ->
            SettingsSectionItem(section = section)
        }

        item {
            Spacer(modifier = Modifier.navigationBarsPadding().size(32.dp))
        }
    }
}

@Composable
private fun SettingsSectionItem(section: SettingsSection, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .platformStyleClickable(onClick = section.onClick)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = section.icon,
                    contentDescription = null,
                    tint = if (section.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (section.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = section.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (section.isDestructive) {
                            MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        }
                    )
                }
            }
        }
    }
}