package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
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
import com.gaoyun.yanyou_kototomo.util.toReviewRelativeShortFormat
import kotlinx.datetime.LocalDate

@Composable
internal fun Transcription(transcription: String, preformatted: Boolean = false) {
    val transcriptionFormatted = if (preformatted) transcription else "[$transcription]"
    Text(
        text = transcriptionFormatted,
        style = MaterialTheme.typography.bodyMedium,
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
    dynamic: Boolean = true,
    style: TextStyle = MaterialTheme.typography.displayMedium,
    fontSizeMax: TextUnit = 62.sp,
    modifier: Modifier = Modifier,
    leftAttachment: @Composable RowScope.() -> Unit = {},
    rightAttachment: @Composable RowScope.() -> Unit = {},
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        leftAttachment()
        if (dynamic) {
            AutoResizeText(
                text = front,
                fontSizeRange = FontSizeRange(min = 16.sp, max = fontSizeMax),
                style = style.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = fontSizeMax
                ),
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = modifier
            )
        } else {
            Text(
                text = front,
                style = style.copy(fontWeight = FontWeight.Medium),
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
        rightAttachment()
    }
}

@Composable
internal fun RowScope.Reading(
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
    nextReviewDate: LocalDate?,
    content: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier
            .wrapContentHeight()
            .platformStyleClickable { onClick() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                nextReviewDate?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp, top = 4.dp)
                    ) {
                        Text(
                            text = it.toReviewRelativeShortFormat(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.EventRepeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                content()
            }
        }
    }
}

internal fun Card.emptySpacesAfter(): Int {
    val oneSpaceFor = listOf("や", "ゆ", "ヤ", "ユ")
    val threeSpacesFor = listOf("わ", "ワ")
    return when {
        oneSpaceFor.contains(front) -> 3
        threeSpacesFor.contains(front) -> 9
        else -> 0
    }
}