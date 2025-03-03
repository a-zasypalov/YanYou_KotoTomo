package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.util.AppIcon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.app_icon_variant_1
import yanyou_kototomo.composeapp.generated.resources.app_icon_variant_2
import yanyou_kototomo.composeapp.generated.resources.app_icon_variant_3
import yanyou_kototomo.composeapp.generated.resources.clip_icon_description
import yanyou_kototomo.composeapp.generated.resources.clip_icon_title
import yanyou_kototomo.composeapp.generated.resources.main_icon_description
import yanyou_kototomo.composeapp.generated.resources.main_icon_title
import yanyou_kototomo.composeapp.generated.resources.simple_icon_description
import yanyou_kototomo.composeapp.generated.resources.simple_icon_title

@Composable
fun ColumnScope.AppIconSettingScreenContent(onIconSetClick: (AppIcon) -> Unit) {
    val icons = listOf(
        AppIconVariants(
            AppIcon.Original,
            Res.drawable.app_icon_variant_1,
            stringResource(Res.string.main_icon_title),
            stringResource(Res.string.main_icon_description),
        ),
        AppIconVariants(
            AppIcon.Clip,
            Res.drawable.app_icon_variant_2,
            stringResource(Res.string.clip_icon_title),
            stringResource(Res.string.clip_icon_description),
        ),
        AppIconVariants(
            AppIcon.VerticalHalf,
            Res.drawable.app_icon_variant_3,
            stringResource(Res.string.simple_icon_title),
            stringResource(Res.string.simple_icon_description),
        ),
    )

    icons.forEach { item ->
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .platformStyleClickable(onClick = { onIconSetClick(item.iconType) })
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(shadowElevation = 8.dp, shape = MaterialTheme.shapes.medium) {
                        Image(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(72.dp)
                        )
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

private data class AppIconVariants(
    val iconType: AppIcon,
    val icon: DrawableResource,
    val title: String,
    val subtitle: String,
)