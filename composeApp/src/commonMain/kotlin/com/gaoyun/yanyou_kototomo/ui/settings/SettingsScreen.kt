package com.gaoyun.yanyou_kototomo.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.domain.toStringRes
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSections
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToOnboarding
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToSettingsSection
import com.gaoyun.yanyou_kototomo.ui.card_details.getRandomMascotImage
import com.gaoyun.yanyou_kototomo.ui.settings.sections.SettingsViewModel
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = SettingsViewModel::class)

    var showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getSettings()
    }

    viewModel.viewState.collectAsState().value?.let { viewState ->
        SettingsScreenContent(
            modifier = modifier,
            primaryLanguage = stringResource(viewState.primaryLanguageId.toStringRes()),
            onAppIconClick = { navigate(ToSettingsSection(SettingsSections.AppIcon)) },
            onColorThemeClick = { navigate(ToSettingsSection(SettingsSections.ColorTheme)) },
            onAboutAppClick = { navigate(ToSettingsSection(SettingsSections.AboutApp)) },
            onOnboardingClick = { navigate(ToOnboarding) },
            onPrimaryLanguageChange = viewModel::setPrimaryLanguageId,
            availableLanguages = viewState.availableLanguages.map { it to stringResource(it.toStringRes()) },
            onResetClick = { showDialog.value = true },
        )
    }

    ResetDialog(showDialog, viewModel::resetAllData)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    modifier: Modifier,
    primaryLanguage: String,
    availableLanguages: List<Pair<LanguageId, String>>,
    onAppIconClick: () -> Unit,
    onColorThemeClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onOnboardingClick: () -> Unit,
    onPrimaryLanguageChange: (LanguageId) -> Unit,
    onResetClick: () -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedLanguage = remember { mutableStateOf(primaryLanguage) }

    val onPrimaryLanguageChangeClick = {
        showDialog.value = true
    }

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
                onPrimaryLanguageChangeClick,
                onAppIconClick,
                onColorThemeClick,
                onAboutAppClick,
                onOnboardingClick,
                onResetClick
            )
        ) { section ->
            SettingsSectionItem(section = section)
        }
    }

    // Dialog for selecting primary language
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Select Primary Language") },
            text = {
                Column {
                    availableLanguages.forEach { (id, languageName) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id.getRandomMascotImage()),
                                null,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                            Text(
                                text = languageName,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .platformStyleClickable {
                                        selectedLanguage.value = languageName
                                        showDialog.value = false
                                        onPrimaryLanguageChange(id)
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
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

@Composable
fun ResetDialog(showDialog: MutableState<Boolean>, onResetConfirmed: () -> Unit) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    onResetConfirmed()
                }) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Reset") },
            text = { Text("Are you sure you want to reset all data in the app? This action cannot be undone.") },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}

data class SettingsSection(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false,
)