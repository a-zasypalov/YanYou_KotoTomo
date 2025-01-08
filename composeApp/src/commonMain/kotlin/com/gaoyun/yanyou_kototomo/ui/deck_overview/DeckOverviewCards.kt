package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import kotlinx.datetime.LocalDate

@Composable
fun DeckOverviewWordCard(
    card: Card.WordCard,
    showTranslation: Boolean,
    nextReviewDate: LocalDate?,
    showTranscription: Boolean,
    onClick: () -> Unit, modifier: Modifier = Modifier,
) {
    DeckCard(nextReviewDate = nextReviewDate, onClick = onClick, modifier = modifier.height(180.dp)) {
        CardFront(card.front)
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
    nextReviewDate: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(nextReviewDate = nextReviewDate, onClick = onClick, modifier = modifier.heightIn(max = 100.dp), contentPadding = 0.dp) {
        CardFront(front = card.front, fontSizeMax = 48.sp)
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
    nextReviewDate: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(nextReviewDate = nextReviewDate, onClick = onClick, modifier = modifier.height(180.dp)) {
        CardFront(
            front = card.front,
            modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
            leftAttachment = {
                this@DeckCard.AnimatedVisibility(visible = showReading, modifier = modifier.align(Alignment.CenterStart)) {
                    Reading(card.reading.on)
                }
            },
            rightAttachment = {
                this@DeckCard.AnimatedVisibility(visible = showReading, modifier = modifier.align(Alignment.CenterEnd)) {
                    Reading(card.reading.kun)
                }
            })
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
    nextReviewDate: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(nextReviewDate = nextReviewDate, onClick = onClick, modifier = modifier.height(180.dp)) {
        CardFront(card.front)
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}