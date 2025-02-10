package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository

class CardProgressUpdater(private val repository: CardsAndProgressRepository) {

    fun updateCardProgress(cardProgress: CardProgress, deckId: DeckId) {
        repository.updateProgress(cardProgress, deckId)
    }

    fun updateCardCompletion(cardId: CardId, deckId: DeckId, completed: Boolean) {
        repository.updateCardCompletion(cardId, deckId, completed)
    }

    fun resetDeck(deckId: DeckId) = repository.resetDeck(deckId)
}