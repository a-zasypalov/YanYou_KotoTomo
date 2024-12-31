package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable

@Composable
internal fun Transcription(transcription: String, preformatted: Boolean = false) {
    val transcriptionFormatted = if (preformatted) transcription else "[$transcription]"
    Text(
        text = transcriptionFormatted,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(4.dp),
    )
}

@Composable
internal fun Translation(translation: String) {
    AutoResizeText(
        text = translation,
        fontSizeRange = FontSizeRange(min = 12.sp, max = MaterialTheme.typography.bodyLarge.fontSize),
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 1,
        textAlign = TextAlign.Center,
    )
}


@Composable
internal fun ColumnScope.CardFront(
    front: String,
    fontSizeMax: TextUnit = 62.sp,
    modifier: Modifier = Modifier,
    rightAttachment: @Composable BoxScope.() -> Unit = {},
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.weight(1f)) {
        AutoResizeText(
            text = front,
            fontSizeRange = FontSizeRange(min = 16.sp, max = fontSizeMax),
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = fontSizeMax
            ),
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
        )
        rightAttachment()
    }
}

@Composable
internal fun BoxScope.Reading(
    reading: List<Card.KanaCard>,
    modifier: Modifier = Modifier,
) {
    Text(
        text = reading.map { it.front }.joinToString("\n"),
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = 18.sp,
        modifier = modifier,
    )
}

@Composable
internal fun DeckCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentPadding: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .platformStyleClickable { onClick() }
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

internal fun Card.KanaCard.emptySpacesAfter(): Int {
    val oneSpaceFor = listOf("や", "ゆ", "ヤ", "ユ")
    val threeSpacesFor = listOf("わ", "ワ")
    return when {
        oneSpaceFor.contains(front) -> 1
        threeSpacesFor.contains(front) -> 3
        else -> 0
    }
}