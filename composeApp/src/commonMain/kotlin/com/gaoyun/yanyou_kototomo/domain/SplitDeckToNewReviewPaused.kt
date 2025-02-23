package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.completed
import com.gaoyun.yanyou_kototomo.data.local.card.hasProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.ui_state.DeckPart
import com.gaoyun.yanyou_kototomo.data.ui_state.DeckSplitResult

object SplitDeckToNewReviewPaused {

    @Suppress("UNCHECKED_CAST")
    fun splitDeckToNewReviewPaused(
        cards: List<CardWithProgress<*>>,
        settings: DeckSettings,
    ): DeckSplitResult.Base {
        val pausedCardIds = settings.pausedCards.toSet()
        val pausedCards = mutableListOf<CardWithProgress.Base<*>>()
        val cardsToReview = mutableListOf<CardWithProgress.Base<*>>()
        val completedCards = mutableListOf<CardWithProgress.Base<*>>()

        val newWords = mutableListOf<CardWithProgress<Card.WordCard>>()
        val newPhrases = mutableListOf<CardWithProgress<Card.PhraseCard>>()
        val newKanji = mutableListOf<CardWithProgress<Card.KanjiCard>>()
        val newKana = mutableListOf<CardWithProgress<Card.KanaCard>>()

        for (card in cards) {
            when {
                pausedCardIds.contains(card.card.id.identifier) -> pausedCards.add(card.base())
                card.hasProgress() && !card.completed() && card.card !is Card.KanaCard -> cardsToReview.add(card.base())
                card.completed() && card.card !is Card.KanaCard -> completedCards.add(card.base())
                else -> when (card.card) {
                    is Card.WordCard -> newWords.add(card as CardWithProgress<Card.WordCard>)
                    is Card.PhraseCard -> newPhrases.add(card as CardWithProgress<Card.PhraseCard>)
                    is Card.KanjiCard -> newKanji.add(card as CardWithProgress<Card.KanjiCard>)
                    is Card.KanaCard -> newKana.add(card as CardWithProgress<Card.KanaCard>)
                }
            }
        }

        return DeckSplitResult.Base(
            deckId = settings.deckId,
            newCards = DeckPart(newKanji, newWords, newPhrases, newKana),
            cardsToReview = cardsToReview.sortedBy { it.progress?.nextReview },
            pausedCards = pausedCards,
            completedCards = completedCards
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun splitDeckToPersonalState(
        cards: List<CardWithProgress.WithDeckInfo<*>>,
        settings: DeckSettings,
    ): DeckSplitResult.WithDeckInfo {
        val pausedCardIds = settings.pausedCards.toSet()
        val pausedCards = mutableListOf<CardWithProgress.WithDeckInfo<*>>()
        val cardsToReview = mutableListOf<CardWithProgress.WithDeckInfo<*>>()
        val completedCards = mutableListOf<CardWithProgress.WithDeckInfo<*>>()
        val newCards = mutableListOf<CardWithProgress.WithDeckInfo<*>>()

        for (card in cards) {
            when {
                pausedCardIds.contains(card.card.id.identifier) -> pausedCards.add(card)
                card.hasProgress() && !card.completed() -> cardsToReview.add(card)
                card.completed() -> completedCards.add(card)
                else -> newCards.add(card)
            }
        }

        return DeckSplitResult.WithDeckInfo(
            deckId = settings.deckId,
            newCards = newCards,
            cardsToReview = cardsToReview.sortedBy { it.progress?.nextReview },
            pausedCards = pausedCards,
            completedCards = completedCards
        )
    }
}