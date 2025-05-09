package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.util.toReviewRelativeShortFormat
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.active
import yanyou_kototomo.composeapp.generated.resources.completed
import yanyou_kototomo.composeapp.generated.resources.kanji
import yanyou_kototomo.composeapp.generated.resources.phrases
import yanyou_kototomo.composeapp.generated.resources.words

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckPausedItemsSettingsView(
    allCards: State<List<CardWithProgress<*>>?>,
    pausedCards: MutableState<List<CardWithProgress<*>>?>,
    onCardPause: ((CardWithProgress<*>, Boolean) -> Unit),
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    fun changePausedStateFor(card: CardWithProgress<*>, currentPausedState: Boolean) {
        pausedCards.value = (pausedCards.value ?: listOf()).toMutableList().apply {
            if (currentPausedState) remove(card) else add(card)
        }
        onCardPause(card, !currentPausedState)
    }

    allCards.value?.let { cardsWithProgress ->
        val kanji = remember { cardsWithProgress.filter { it.card is Card.KanjiCard && !it.isCompleted() } }
        val phrases = remember { cardsWithProgress.filter { it.card is Card.PhraseCard && !it.isCompleted() } }
        val words = remember { cardsWithProgress.filter { it.card is Card.WordCard && !it.isCompleted() } }
        val completed = remember { cardsWithProgress.filter { it.isCompleted() } }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                if (kanji.isNotEmpty()) {
                    item { SectionHeader(stringResource(Res.string.kanji)) }
                    items(kanji) { cardWithProgress ->
                        PausedItemCard(
                            cardWithProgress = cardWithProgress,
                            pausedCards = pausedCards,
                            onChangePausedState = { changePausedStateFor(it, pausedCards.value?.contains(it) == true) }
                        )
                    }
                }
                if (words.isNotEmpty()) {
                    item { SectionHeader(stringResource(Res.string.words), stringResource(Res.string.active)) }
                    items(words) { cardWithProgress ->
                        PausedItemCard(
                            cardWithProgress = cardWithProgress,
                            pausedCards = pausedCards,
                            onChangePausedState = { changePausedStateFor(it, pausedCards.value?.contains(it) == true) }
                        )
                    }
                }
                if (phrases.isNotEmpty()) {
                    item {
                        SectionHeader(
                            stringResource(Res.string.phrases),
                            stringResource(Res.string.active),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(phrases) { cardWithProgress ->
                        PausedItemCard(
                            cardWithProgress = cardWithProgress,
                            pausedCards = pausedCards,
                            onChangePausedState = { changePausedStateFor(it, pausedCards.value?.contains(it) == true) }
                        )
                    }
                }
                if (completed.isNotEmpty()) {
                    item {
                        SectionHeader(
                            stringResource(Res.string.completed),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(completed) { cardWithProgress ->
                        PausedItemCard(
                            cardWithProgress = cardWithProgress,
                            pausedCards = pausedCards,
                            onChangePausedState = {}
                        )
                    }
                }
                item { Spacer(modifier = Modifier.navigationBarsPadding().size(32.dp)) }
            }
        }
    }
}

@Composable
fun SectionHeader(name: String, status: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Divider(1.dp, modifier = Modifier.fillMaxWidth())
        status?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
    }
}

@Composable
fun PausedItemCard(
    cardWithProgress: CardWithProgress<*>,
    pausedCards: MutableState<List<CardWithProgress<*>>?>,
    onChangePausedState: (CardWithProgress<*>) -> Unit,
) {
    val card = cardWithProgress.card
    val paused = pausedCards.value?.contains(cardWithProgress) == true
    val subtitle = if (card is Card.KanaCard) card.transcription else card.translationOrEmpty()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .platformStyleClickable { onChangePausedState(cardWithProgress) },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = card.front,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

            if (!cardWithProgress.isCompleted()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    cardWithProgress.progress?.nextReview?.let { nextReview ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventRepeat,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = nextReview.toReviewRelativeShortFormat(),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }

                    Checkbox(
                        checked = !paused,
                        onCheckedChange = { onChangePausedState(cardWithProgress) }
                    )
                }
            }
        }
    }
}