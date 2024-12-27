package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toCardsDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO

class CardsAndProgressRepository(
    private val db: YanYouKotoTomoDatabase,
) {
    companion object {
        const val PAGE_SIZE = 50L
    }

    fun getCardProgressFor(deckId: DeckId): List<CardProgress> {
        val progress = db.card_progressQueries.getDeckProgress(deckId.identifier).executeAsList()
        return progress.map { it.toLocal() }
    }

    fun getCardProgressPage(page: Int): List<CardProgress> {
        println("here for cards")
        val progress = db.card_progressQueries.getAllCardsProgress(PAGE_SIZE, PAGE_SIZE * page).executeAsList()
        return progress.map { it.toLocal() }
    }

    fun getCards(ids: List<String>): List<CardDTO> {
        return db.decksQueries.getCardsByIds(ids).executeAsList().map { it.toCardsDTO() }
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

    fun resetDeck(deckId: DeckId) {
        db.card_progressQueries.removeProgressForDeck(deckId.identifier)
    }
}