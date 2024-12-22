package com.gaoyun.yanyou_kototomo.ui.player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange


@Composable
internal fun CardPlayerTranscription(
    transcription: String,
    modifier: Modifier = Modifier,
    preformatted: Boolean = false,
) {
    val transcriptionFormatted = if (preformatted) transcription else "[$transcription]"
    Text(
        text = transcriptionFormatted,
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(4.dp),
    )
}

@Composable
internal fun CardPlayerTranslation(translation: String) {
    Text(
        text = translation,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
internal fun CardPlayerAdditionalInfo(info: String) {
    Text(
        text = info,
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
internal fun ColumnScope.CardPlayerFront(
    front: String,
    fontSizeMax: TextUnit = 150.sp,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = front,
        transitionSpec = {
            (fadeIn(animationSpec = tween(300)) + scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(300)
            )) togetherWith (fadeOut(animationSpec = tween(200)) + scaleOut(
                targetScale = 1.1f,
                animationSpec = tween(200)
            ))
        },
        label = "Fade Transition"
    ) { targetText ->
        AutoResizeText(
            text = targetText,
            fontSizeRange = FontSizeRange(min = 16.sp, max = fontSizeMax),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = fontSizeMax
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}

@Composable
internal fun ColumnScope.CardPlayerReading(
    reading: Card.KanjiCard.Reading,
    modifier: Modifier = Modifier,
) {
    val readingFormatted = reading.let {
        "${it.on.map { it.front }.joinToString("")}、${it.kun.map { it.front }.joinToString("")}"
    }
    val readingTranscriptionFormatted = reading.let {
        "[${it.on.map { it.transcription }.joinToString("")}, ${it.kun.map { it.transcription }.joinToString("")}]"
    }
    Text(
        text = readingFormatted,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp, top = 16.dp),
    )
    Text(
        text = readingTranscriptionFormatted,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(horizontal = 4.dp).padding(bottom = 8.dp),
    )
}