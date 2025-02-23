package com.gaoyun.yanyou_kototomo.data.ui_state

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress

sealed interface DeckSplitResult {
    data class Base(
        val deckId: DeckId,
        val newCards: DeckPart,
        val cardsToReview: List<CardWithProgress.Base<*>>,
        val pausedCards: List<CardWithProgress.Base<*>>,
        val completedCards: List<CardWithProgress.Base<*>>,
    )

    data class WithDeckInfo(
        val deckId: DeckId,
        val newCards: List<CardWithProgress.WithDeckInfo<*>>,
        val cardsToReview: List<CardWithProgress.WithDeckInfo<*>>,
        val pausedCards: List<CardWithProgress.WithDeckInfo<*>>,
        val completedCards: List<CardWithProgress.WithDeckInfo<*>>,
    )
}

data class DeckPart(
    val kanji: List<CardWithProgress<Card.KanjiCard>>,
    val words: List<CardWithProgress<Card.WordCard>>,
    val phrases: List<CardWithProgress<Card.PhraseCard>>,
    val kana: List<CardWithProgress<Card.KanaCard>>,
) {
    fun size() = kanji.size + words.size + phrases.size + kana.size
}