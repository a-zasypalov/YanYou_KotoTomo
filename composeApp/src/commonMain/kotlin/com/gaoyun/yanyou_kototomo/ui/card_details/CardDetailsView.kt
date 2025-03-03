package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EventRepeat
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
import com.gaoyun.yanyou_kototomo.domain.mapIntervalToColor
import com.gaoyun.yanyou_kototomo.util.toReviewRelativeShortFormat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.card_review_datetime
import yanyou_kototomo.composeapp.generated.resources.completed
import yanyou_kototomo.composeapp.generated.resources.current_interval_days

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CardDetailsView(
    cardState: State<CardWithProgress<*>?>,
    languageId: LanguageId,
    paused: MutableState<Boolean> = mutableStateOf(false),
    completed: MutableState<Boolean> = mutableStateOf(false),
    onCardPause: ((CardWithProgress<*>, Boolean) -> Unit)? = null,
    onCardComplete: ((CardWithProgress<*>, Boolean) -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cardCompleteDialogVisibleState = remember { mutableStateOf(false) }

    cardState.value?.let { cardWithProgress ->
        val showPauseButton = cardWithProgress.card !is Card.KanaCard && completed.value != true && onCardPause != null
        val showCompletionButton = paused.value != true && onCardComplete != null
        val mascotForCard = remember { languageId.getRandomMascotImage() } //Should be here to generate every time new mascot

        CompleteCardConfirmationDialog(cardCompleteDialogVisibleState) {
            onCardComplete?.invoke(cardWithProgress, !completed.value)
            completed.value = !completed.value
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    cardWithProgress.progress?.let { progress ->
                        if (completed.value) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(Res.string.completed),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            )
                        } else {
                            progress.interval?.let { interval ->
                                Box(modifier = Modifier.size(20.dp).background(color = mapIntervalToColor(interval), shape = CircleShape))

                                Text(
                                    text = pluralStringResource(Res.plurals.current_interval_days, interval, interval),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            progress.nextReview?.let { nextReviewDate ->
                                Text(
                                    text = stringResource(Res.string.card_review_datetime, nextReviewDate.toReviewRelativeShortFormat()),
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
                    }
                }

                when (val card = cardWithProgress.card) {
                    is Card.WordCard -> CardDetailsWord(card)
                    is Card.PhraseCard -> CardDetailsPhraseCard(card)
                    is Card.KanjiCard -> CardDetailsKanjiCard(card)
                    is Card.KanaCard -> CardDetailsKanaCard(card)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)) {
                    if (showPauseButton) {
                        CardPauseButton(paused.value, modifier = Modifier.weight(1f)) {
                            onCardPause(cardWithProgress, !paused.value)
                            paused.value = !paused.value
                        }
                    }
                    if (showCompletionButton) {
                        CardCompleteButton(completed.value, modifier = Modifier.weight(1f)) {
                            if (completed.value) {
                                onCardComplete.invoke(cardWithProgress, !completed.value)
                                completed.value = !completed.value
                            } else {
                                cardCompleteDialogVisibleState.value = true
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.size(32.dp))

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