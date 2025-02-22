package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.repository.DeckSettingsRepository

class DeckSettingsInteractor(private val repository: DeckSettingsRepository) {
    fun getAllDeckSettings(): List<DeckSettings> = repository.getAllDeckSettings()
    fun getDeckSettings(deckId: DeckId): DeckSettings? = repository.getDeckSettings(deckId)
    fun updateDeckSettings(settings: DeckSettings) = repository.saveDeckSettings(settings)
}