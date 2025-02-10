package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import kotlinx.datetime.LocalDate

@Composable
fun DeckOverviewWordCard(
    card: Card.WordCard,
    showTranslation: Boolean,
    intervalInDays: Int?,
    nextReviewDate: LocalDate?,
    showTranscription: Boolean,
    completed: Boolean,
    onClick: () -> Unit, modifier: Modifier = Modifier,
) {
    DeckCard(
        nextReviewDate = nextReviewDate,
        intervalInDays = intervalInDays,
        onClick = onClick,
        completed = completed,
        modifier = modifier.height(180.dp)
    ) {
        CardFront(card.front, modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}

@Composable
fun DeckOverviewKanaCard(
    card: Card.KanaCard,
    showTranscription: Boolean,
    intervalInDays: Int?,
    nextReviewDate: LocalDate?,
    completed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(
        nextReviewDate = nextReviewDate,
        intervalInDays = intervalInDays,
        showDate = false,
        onClick = onClick,
        modifier = modifier.height(100.dp),
        completed = completed,
        contentPadding = 0.dp
    ) {
        CardFront(front = card.front, dynamic = false, style = MaterialTheme.typography.displayMedium, modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = showTranscription) {
            Transcription(
                transcription = "[${card.transcription}] ${card.mirror.front}",
                preformatted = true
            )
        }

    }
}

@Composable
fun DeckOverviewKanjiCard(
    card: Card.KanjiCard,
    showTranslation: Boolean,
    showTranscription: Boolean,
    showReading: Boolean,
    intervalInDays: Int?,
    nextReviewDate: LocalDate?,
    completed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(
        nextReviewDate = nextReviewDate,
        intervalInDays = intervalInDays,
        onClick = onClick,
        completed = completed,
        modifier = modifier.height(180.dp)
    ) {
        CardFront(
            front = card.front,
            modifier = Modifier.weight(1f),
            dynamic = false,
            style = MaterialTheme.typography.displayLarge,
            leftAttachment = {
                this@DeckCard.AnimatedVisibility(visible = showReading) {
                    Reading(card.reading.on)
                }
            },
            rightAttachment = {
                this@DeckCard.AnimatedVisibility(visible = showReading) {
                    Reading(card.reading.kun)
                }
            }
        )
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription()) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}

@Composable
fun DeckOverviewPhraseCard(
    card: Card.PhraseCard,
    showTranslation: Boolean,
    showTranscription: Boolean,
    intervalInDays: Int?,
    nextReviewDate: LocalDate?,
    completed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(
        nextReviewDate = nextReviewDate,
        intervalInDays = intervalInDays,
        onClick = onClick,
        completed = completed,
        modifier = modifier.height(180.dp)
    ) {
        CardFront(card.front, modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}