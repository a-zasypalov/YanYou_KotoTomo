package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.util.toRelativeFormat
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CardDetailsView(
    cardState: State<CardWithProgress<*>?>,
    languageId: LanguageId,
    paused: MutableState<Boolean> = mutableStateOf(false),
    onCardPause: ((CardWithProgress<*>, Boolean) -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    cardState.value?.let { cardWithProgress ->
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            windowInsets = WindowInsets(0),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                cardWithProgress.progress?.nextReview?.let { nextReviewDate ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "review ${nextReviewDate.toRelativeFormat()}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.EventRepeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                when (val card = cardWithProgress.card) {
                    is Card.WordCard -> CardDetailsWord(card)
                    is Card.PhraseCard -> CardDetailsPhraseCard(card)
                    is Card.KanjiCard -> CardDetailsKanjiCard(card)
                    is Card.KanaCard -> CardDetailsKanaCard(card)
                }

                Spacer(modifier = Modifier.weight(1f))

                if (cardWithProgress.card !is Card.KanaCard && onCardPause != null) {
                    CardPauseButton(paused.value, modifier = Modifier.fillMaxWidth()) {
                        onCardPause(cardWithProgress, !paused.value)
                        paused.value = !paused.value
                    }
                }

                Spacer(modifier = Modifier.size(32.dp))

                val mascotForCard = remember { languageId.getRandomMascotImage() }
                Image(
                    painter = painterResource(mascotForCard),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally).alpha(0.4f)
                )

                Spacer(modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
private fun CardPauseButton(paused: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    if (paused) {
        PrimaryElevatedButton(
            text = "Bring back to deck",
            onClick = onClick,
            leadingIcon = Icons.AutoMirrored.Default.Undo,
            modifier = modifier
        )
    } else {
        PrimaryElevatedButton(
            text = "Pause this card",
            onClick = onClick,
            leadingIcon = Icons.Default.Pause,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = modifier
        )
    }
}

@Composable
private fun ColumnScope.CardDetailsWord(card: Card.WordCard) {
    CardDetailsFront(card.front, modifier = Modifier.fillMaxWidth())
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation, modifier = Modifier.fillMaxWidth())
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}

@Composable
private fun ColumnScope.CardDetailsKanaCard(card: Card.KanaCard) {
    CardDetailsFront(front = card.front)
    CardDetailsTranscription(
        transcription = "[${card.transcription}] ${card.mirror.front}",
        preformatted = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ColumnScope.CardDetailsKanjiCard(card: Card.KanjiCard) {
    CardDetailsFront(
        front = card.front,
        modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
        leftAttachment = { CardDetailsReading(card.reading.on, Modifier.align(Alignment.CenterStart)) },
        rightAttachment = { CardDetailsReading(card.reading.kun, Modifier.align(Alignment.CenterEnd)) }
    )
    CardDetailsTranscription(card.transcription(), modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation, modifier = Modifier.fillMaxWidth())
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}

@Composable
private fun ColumnScope.CardDetailsPhraseCard(card: Card.PhraseCard) {
    CardDetailsFront(card.front)
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation, modifier = Modifier.fillMaxWidth())
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}