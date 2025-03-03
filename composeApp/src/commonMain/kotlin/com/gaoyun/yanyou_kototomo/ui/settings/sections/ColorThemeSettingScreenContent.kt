package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.util.AppTheme
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.color_theme_blue_description
import yanyou_kototomo.composeapp.generated.resources.color_theme_blue_title
import yanyou_kototomo.composeapp.generated.resources.color_theme_green_description
import yanyou_kototomo.composeapp.generated.resources.color_theme_green_title
import yanyou_kototomo.composeapp.generated.resources.color_theme_main_description
import yanyou_kototomo.composeapp.generated.resources.color_theme_main_title

@Composable
fun ColumnScope.ColorThemeSettingScreenContent(onThemeSet: (AppTheme) -> Unit) {
    val themes = listOf(
        ColorThemeVariants(
            AppTheme.Original, Color(0xFFFFF8F3),
            stringResource(Res.string.color_theme_main_title),
            stringResource(Res.string.color_theme_main_description),
        ),
        ColorThemeVariants(
            AppTheme.Blue, Color(0xFFb1d8f2),
            stringResource(Res.string.color_theme_blue_title),
            stringResource(Res.string.color_theme_blue_description),
        ),
        ColorThemeVariants(
            AppTheme.Green, Color(0xFFd6f2b1),
            stringResource(Res.string.color_theme_green_title),
            stringResource(Res.string.color_theme_green_description),
        ),
    )

    themes.forEach { item ->
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .platformStyleClickable(onClick = { onThemeSet(item.theme) })
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shadowElevation = 8.dp,
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Box(modifier = Modifier.size(72.dp).background(item.color))
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            Divider(height = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

private data class ColorThemeVariants(
    val theme: AppTheme,
    val color: Color,
    val title: String,
    val subtitle: String,
)