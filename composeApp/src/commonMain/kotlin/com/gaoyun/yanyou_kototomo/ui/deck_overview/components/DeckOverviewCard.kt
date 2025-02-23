package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings

@Composable
fun DeckOverviewCard(
    cardWithProgress: CardWithProgress<*>,
    settings: DeckSettings,
    onCardClick: (CardWithProgress<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val card = cardWithProgress.card
    when (card) {
        is Card.WordCard ->
            DeckOverviewWordCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                onClick = { onCardClick(cardWithProgress) },
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                modifier = modifier
            )

        is Card.PhraseCard ->
            DeckOverviewPhraseCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )

        is Card.KanjiCard ->
            DeckOverviewKanjiCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                showReading = settings.showReading,
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )

        is Card.KanaCard -> {
            DeckOverviewKanaCard(
                card = card,
                showTranscription = settings.showTranscription,
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )
        }
    }
}