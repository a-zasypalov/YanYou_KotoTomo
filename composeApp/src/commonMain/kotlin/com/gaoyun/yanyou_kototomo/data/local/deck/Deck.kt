package com.gaoyun.yanyou_kototomo.data.local.deck

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress

data class Deck(
    val id: DeckId,
    val name: String,
    val cards: List<CardWithProgress<*>>,
) {
    private val kanaDecks = listOf("hiragana_en", "katakana_en")

    fun isKanaDeck() = kanaDecks.contains(id.identifier)
    fun isJlptDeck() = id.identifier.contains("jlpt")
}