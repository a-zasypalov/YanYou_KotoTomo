package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal

class CardProgressRepository(
    private val db: YanYouKotoTomoDatabase,
) {
    fun getCardProgressFor(deckId: DeckId): List<CardProgress> {
        val progress = db.card_progressQueries.getDeckProgress(deckId.identifier).executeAsList()
        return progress.map { it.toLocal() }
    }

    fun updateProgress(progress: CardProgress, deckId: DeckId) {
        db.card_progressQueries.updateCardProgress(
            lastReviewed = progress.lastReviewed.toString(),
            interval = progress.interval.toLong(),
            easeFactor = progress.easeFactor,
            nextReview = progress.nextReview.toString(),
            card_id = progress.cardId,
            deck_id = deckId.identifier
        )
    }
}