package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.DeckSettings
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase

class DeckSettingsRepository(
    private val db: YanYouKotoTomoDatabase,
) {
    fun saveDeckSettings(deckSettings: DeckSettings) {
        db.deck_settingsQueries.updateSettings(
            deckId = deckSettings.deckId.identifier,
            showTranslation = if (deckSettings.showTranslation) 1 else 0,
            showTranscription = if (deckSettings.showTranscription) 1 else 0,
            showReading = if (deckSettings.showReading) 1 else 0
        )
    }

    fun getDeckSettings(deckId: DeckId): DeckSettings? {
        return db.deck_settingsQueries
            .getSettingForDeck(deckId.identifier)
            .executeAsOneOrNull()
            ?.let {
                DeckSettings(
                    deckId = DeckId(it.deckId),
                    showTranslation = it.showTranslation == 1L,
                    showTranscription = it.showTranscription == 1L,
                    showReading = it.showReading == 1L
                )
            }
    }
}