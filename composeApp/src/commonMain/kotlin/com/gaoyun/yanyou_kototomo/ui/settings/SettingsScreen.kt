package com.gaoyun.yanyou_kototomo.ui.settings

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSections
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToSettingsSection

@Composable
fun SettingsScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    SettingsScreenContent(
        modifier = modifier,
        onAppIconClick = { navigate(ToSettingsSection(SettingsSections.AppIcon)) },
        onColorThemeClick = { navigate(ToSettingsSection(SettingsSections.ColorTheme)) },
        onAboutAppClick = { navigate(ToSettingsSection(SettingsSections.AboutApp)) },
        onResetClick = {}
    )
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier,
    onAppIconClick: () -> Unit,
    onColorThemeClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
            )
        }

        items(settingsSections(onAppIconClick, onColorThemeClick, onAboutAppClick, onResetClick)) { section ->
            SettingsSectionItem(section = section)
        }
    }
}

@Composable
private fun SettingsSectionItem(section: SettingsSection) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
//        Divider(height = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
    }
}

data class SettingsSection(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false, // For special styles like "Reset"
)