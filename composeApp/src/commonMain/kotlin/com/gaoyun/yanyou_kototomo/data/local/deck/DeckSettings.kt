package com.gaoyun.yanyou_kototomo.data.local.deck

import com.gaoyun.yanyou_kototomo.data.local.DeckId

data class DeckSettings(
    val deckId: DeckId,
    val showTranslation: Boolean,
    val showTranscription: Boolean,
    val showReading: Boolean,
    val hiddenSections: Set<Sections>,
    val pausedCards: Set<String>,
) {
    companion object {
        fun DEFAULT(deckId: DeckId) = DeckSettings(
            deckId = deckId,
            showTranslation = true,
            showTranscription = true,
            showReading = true,
            hiddenSections = setOf(Sections.Paused),
            pausedCards = setOf()
        )
    }

    enum class Sections { Kanji, NewWords, NewPhrases, Review, Paused }
}