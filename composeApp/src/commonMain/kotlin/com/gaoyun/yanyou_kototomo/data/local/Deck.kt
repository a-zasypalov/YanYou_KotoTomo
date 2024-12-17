package com.gaoyun.yanyou_kototomo.data.local

data class Deck(
    val id: DeckId,
    val name: String,
    val cards: List<Card>,
) {
    private val kanaDecks = listOf("hiragana_en", "katakana_en")

    fun isKanaDeck() = kanaDecks.contains(id.identifier)
}