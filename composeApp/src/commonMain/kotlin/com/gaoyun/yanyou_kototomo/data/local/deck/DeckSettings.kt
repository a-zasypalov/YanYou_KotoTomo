package com.gaoyun.yanyou_kototomo.data.local.deck

import com.gaoyun.yanyou_kototomo.data.local.DeckId

data class DeckSettings(
    val deckId: DeckId,
    val showTranslation: Boolean,
    val showTranscription: Boolean,
    val showReading: Boolean,
) {
    companion object {
        fun DEFAULT(deckId: DeckId) = DeckSettings(
            deckId = deckId,
            showTranslation = true,
            showTranscription = true,
            showReading = true
        )
    }
}