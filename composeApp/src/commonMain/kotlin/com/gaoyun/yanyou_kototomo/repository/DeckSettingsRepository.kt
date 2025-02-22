package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase

class DeckSettingsRepository(
    private val db: YanYouKotoTomoDatabase,
) {
    fun saveDeckSettings(deckSettings: DeckSettings) {
        db.deck_settingsQueries.updateSettings(
            deckId = deckSettings.deckId.identifier,
            showTranslation = if (deckSettings.showTranslation) 1 else 0,
            showTranscription = if (deckSettings.showTranscription) 1 else 0,
            showReading = if (deckSettings.showReading) 1 else 0,
            hiddenSections = deckSettings.hiddenSections.map { it.name },
            pausedCards = deckSettings.pausedCards.toList()
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
                    showReading = it.showReading == 1L,
                    pausedCards = it.pausedCards.toSet(),
                    hiddenSections = it.hiddenSections.mapNotNull {
                        try {
                            DeckSettings.Sections.valueOf(it)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            null
                        }
                    }.toSet(),
                )
            }
    }

    fun getAllDeckSettings(): List<DeckSettings> {
        return db.deck_settingsQueries
            .getSettingForDecks()
            .executeAsList()
            .map {
                DeckSettings(
                    deckId = DeckId(it.deckId),
                    showTranslation = it.showTranslation == 1L,
                    showTranscription = it.showTranscription == 1L,
                    showReading = it.showReading == 1L,
                    pausedCards = it.pausedCards.toSet(),
                    hiddenSections = it.hiddenSections.mapNotNull {
                        try {
                            DeckSettings.Sections.valueOf(it)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            null
                        }
                    }.toSet(),
                )
            }
    }
}