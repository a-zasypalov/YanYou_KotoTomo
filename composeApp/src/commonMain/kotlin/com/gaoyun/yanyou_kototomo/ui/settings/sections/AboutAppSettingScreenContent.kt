package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.app_icon_variant_1

@Composable
fun AboutAppSettingScreenContent() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.size(32.dp))

        Surface(shadowElevation = 16.dp, shape = MaterialTheme.shapes.medium) {
            Image(
                painter = painterResource(Res.drawable.app_icon_variant_1),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
        }

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            text = "言友",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "HieroFlash",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = "The original name of this app is YangYou or KotoTomo. It combines two hieroglyphs and depending in which language you read them, they pronouncing differently but have the same meaning.\n\n言 (Koto in Japanese and Yang in Chinese), meaning \"words\" or \"language\".\n\n友 (Tomo or You), meaning \"friend\".\n\nTogether, they represent the idea of a trusted companion to help you master words and characters in Chinese and Japanese. Whether you\'re learning Hanzi or Kanji, HieroFlah is here to make your language journey more enjoyable and effective.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}