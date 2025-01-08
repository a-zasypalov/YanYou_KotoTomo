package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange

@Composable
internal fun CardDetailsTranscription(
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
internal fun CardDetailsTranslation(translation: String, modifier: Modifier = Modifier) {
    Text(
        text = translation,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
internal fun CardDetailsAdditionalInfo(info: String) {
    Text(
        text = info,
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
internal fun ColumnScope.CardDetailsFront(
    front: String,
    fontSizeMax: TextUnit = 150.sp,
    modifier: Modifier = Modifier,
    leftAttachment: @Composable BoxScope.() -> Unit = {},
    rightAttachment: @Composable BoxScope.() -> Unit = {},
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        leftAttachment()
        AutoResizeText(
            text = front,
            fontSizeRange = FontSizeRange(min = 16.sp, max = fontSizeMax),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = fontSizeMax
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
        )
        rightAttachment()
    }
}

@Composable
internal fun BoxScope.CardDetailsReading(
    reading: List<Card.KanaCard>,
    modifier: Modifier = Modifier,
) {
    Text(
        text = reading.map { it.front }.joinToString("\n"),
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(horizontal = 8.dp)
    )
}