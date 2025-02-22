package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress

data class DeckSplitResult(
    val deckId: DeckId,
    val newCards: DeckPart,
    val cardsToReview: List<CardWithProgress<*>>,
    val pausedCards: List<CardWithProgress<*>>,
    val completedCards: List<CardWithProgress<*>>,
)

data class DeckPart(
    val kanji: List<CardWithProgress<Card.KanjiCard>>,
    val words: List<CardWithProgress<Card.WordCard>>,
    val phrases: List<CardWithProgress<Card.PhraseCard>>,
    val kana: List<CardWithProgress<Card.KanaCard>>,
) {
    fun size() = kanji.size + words.size + phrases.size + kana.size
}