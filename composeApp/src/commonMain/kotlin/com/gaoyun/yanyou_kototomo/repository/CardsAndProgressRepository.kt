package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toCardsDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames

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

    fun getCards(ids: List<String>): List<Pair<CardDTO, GetDeckNames?>> {
        val cards = db.decksQueries.getCardsByIds(ids).executeAsList()
        val decks = db.coursesQueries.getDeckNames(cards.map { it.deck_id }).executeAsList()
        return cards.map { it.toCardsDTO() to decks.find { d -> d.id == it.deck_id } }
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

    fun getFullCardsFromCache(cardIds: List<CardId>) = db.decksQueries.getCardsByIds(cardIds.map { it.identifier }).executeAsList()
}