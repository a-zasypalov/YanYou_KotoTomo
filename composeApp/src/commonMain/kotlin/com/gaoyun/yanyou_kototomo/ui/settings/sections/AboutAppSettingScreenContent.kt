package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.app_icon_variant_1
import yanyou_kototomo.composeapp.generated.resources.app_name
import yanyou_kototomo.composeapp.generated.resources.app_name_inspiration
import yanyou_kototomo.composeapp.generated.resources.app_name_jp
import yanyou_kototomo.composeapp.generated.resources.wave

@Composable
fun AboutAppSettingScreenContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).navigationBarsPadding().padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(32.dp))

            Surface(shadowElevation = 16.dp, shape = MaterialTheme.shapes.large) {
                Image(
                    painter = painterResource(Res.drawable.app_icon_variant_1),
                    contentDescription = null,
                    modifier = Modifier.size(96.dp)
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            Text(
                text = stringResource(Res.string.app_name_jp),
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.size(32.dp))

            Surface(
                shadowElevation = 4.dp,
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(Res.string.app_name_inspiration),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                )
            }
        }

        Image(
            painter = painterResource(Res.drawable.wave),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().size(48.dp).alpha(0.4f)
        )
    }
}