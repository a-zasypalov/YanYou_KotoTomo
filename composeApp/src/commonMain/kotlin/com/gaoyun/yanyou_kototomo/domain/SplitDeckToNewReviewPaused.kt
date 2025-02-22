package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.DeckPart
import com.gaoyun.yanyou_kototomo.data.local.DeckSplitResult
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.completed
import com.gaoyun.yanyou_kototomo.data.local.card.hasProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings

object SplitDeckToNewReviewPaused {

    @Suppress("UNCHECKED_CAST")
    fun splitDeckToNewReviewPaused(
        cards: List<CardWithProgress<*>>,
        settings: DeckSettings,
    ): DeckSplitResult {
        val pausedCardIds = settings.pausedCards.toSet()
        val pausedCards = mutableListOf<CardWithProgress<*>>()
        val cardsToReview = mutableListOf<CardWithProgress<*>>()
        val newWords = mutableListOf<CardWithProgress<Card.WordCard>>()
        val newPhrases = mutableListOf<CardWithProgress<Card.PhraseCard>>()
        val newKanji = mutableListOf<CardWithProgress<Card.KanjiCard>>()
        val newKana = mutableListOf<CardWithProgress<Card.KanaCard>>()
        val completedCards = mutableListOf<CardWithProgress<*>>()

        for (card in cards) {
            when {
                pausedCardIds.contains(card.card.id.identifier) -> pausedCards.add(card)
                card.hasProgress() && !card.completed() && card.card !is Card.KanaCard -> cardsToReview.add(card)
                card.completed() && card.card !is Card.KanaCard -> completedCards.add(card)
                else -> when (card.card) {
                    is Card.WordCard -> newWords.add(card as CardWithProgress<Card.WordCard>)
                    is Card.PhraseCard -> newPhrases.add(card as CardWithProgress<Card.PhraseCard>)
                    is Card.KanjiCard -> newKanji.add(card as CardWithProgress<Card.KanjiCard>)
                    is Card.KanaCard -> newKana.add(card as CardWithProgress<Card.KanaCard>)
                }
            }
        }

        return DeckSplitResult(
            deckId = settings.deckId,
            newCards = DeckPart(newKanji, newWords, newPhrases, newKana),
            cardsToReview = cardsToReview.sortedBy { it.progress?.nextReview },
            pausedCards = pausedCards,
            completedCards = completedCards
        )
    }
}