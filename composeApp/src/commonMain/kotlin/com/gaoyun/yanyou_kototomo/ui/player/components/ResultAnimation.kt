package com.gaoyun.yanyou_kototomo.ui.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import yanyou_kototomo.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ColumnScope.ResultAnimation(resultIsSuccess: Boolean?) {
    val height = if (resultIsSuccess == true) 56.dp else 48.dp

    // Show a spacer if the result is null
    if (resultIsSuccess == null) {
        Spacer(modifier = Modifier.size(height))
        return
    }

    val filename = if (resultIsSuccess) "files/quiz_correct.lottie" else "files/quiz_wrong.lottie"
    var isVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible.value = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible.value) {
            val composition = rememberLottieComposition { LottieCompositionSpec.DotLottie(Res.readBytes(filename)) }
            val progress = animateLottieCompositionAsState(composition.value)

            Image(
                painter = rememberLottiePainter(
                    composition = composition.value,
                    progress = { progress.value },
                ),
                contentDescription = null,
                modifier = Modifier.size(height)
            )
        }
    }
}