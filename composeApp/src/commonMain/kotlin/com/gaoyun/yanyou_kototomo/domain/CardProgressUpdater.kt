package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.repository.CardProgressRepository

class CardProgressUpdater(private val repository: CardProgressRepository) {

    fun updateCardProgress(cardProgress: CardProgress, deckId: DeckId) {
        repository.updateProgress(cardProgress, deckId)
    }
}