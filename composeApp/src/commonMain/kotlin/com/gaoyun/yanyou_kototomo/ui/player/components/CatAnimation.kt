package com.gaoyun.yanyou_kototomo.ui.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import yanyou_kototomo.composeapp.generated.resources.Res
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CatAnimation(modifier: Modifier = Modifier) {
    var isVisible = remember { mutableStateOf(false) }
    var loopStarted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Random delay between 5 seconds (5000ms) and 2 minutes (120000ms)
        val randomDelay = Random.nextLong(5000, 120000)
        delay(randomDelay)
        isVisible.value = true
    }

    Box(
        modifier = modifier
            .rotate(180f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible.value) {
            val composition = rememberLottieComposition { LottieCompositionSpec.DotLottie(Res.readBytes("files/cat_looking.lottie")) }
            val progress = animateLottieCompositionAsState(composition.value, reverseOnRepeat = true, iterations = 2)

            Image(
                painter = rememberLottiePainter(
                    composition = composition.value,
                    progress = {
                        when {
                            progress.value < 0.05f -> if (loopStarted.value) isVisible.value = false
                            progress.value > 0.9f -> loopStarted.value = true
                        }
                        progress.value
                    },
                ),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
        }
    }
}