package com.gaoyun.yanyou_kototomo.ui.onboarding.pages

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.app_icon_variant_1

@Composable
fun OnboardingWelcomePage(onNextPage: () -> Unit) {
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
                text = "言友",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.size(4.dp))

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Welcome to HieroFlash",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Your trusted companion to master words and characters from HSK and JLPT books.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )

            Spacer(modifier = Modifier.weight(1.75f))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Surface(
                shadowElevation = 4.dp,
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "言 - words, language.\n友 - friend.",
                        style = MaterialTheme.typography.titleSmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            PrimaryElevatedButton(
                text = "Next",
                onClick = onNextPage,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.navigationBarsPadding().size(16.dp))
        }
    }
}