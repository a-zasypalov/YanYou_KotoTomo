package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.withProgress
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocalDTO
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository

class GetDeckFromCache(
    private val deckRepository: DeckRepository,
    private val cardsAndProgressRepository: CardsAndProgressRepository,
) {

    fun getDeckWithoutName(
        deckId: DeckId,
        requiredDecks: List<DeckId> = listOf(),
    ): Deck? = getDeck(CourseDeck.Normal(deckId, "", "", 0), requiredDecks)

    fun getDeck(
        deck: CourseDeck,
        requiredDecks: List<DeckId> = listOf(),
    ): Deck? {
        val deckResponse = deckRepository.getDeckFromCache(deck.id) ?: return null
        val deckWords = deckResponse.cards.filterIsInstance<CardDTO.WordCardDTO>()
        val deckKana = deckResponse.cards.filterIsInstance<CardDTO.KanaCardDTO>()

        val requiredCards = getRequiredCards(requiredDecks)
        val requiredWords = deckWords + requiredCards.filterIsInstance<CardDTO.WordCardDTO>()
        val kanaCards = deckKana + requiredCards.filterIsInstance<CardDTO.KanaCardDTO>()

        val progresses = cardsAndProgressRepository.getCardProgressFor(deck.id).associateBy { it.cardId }

        val cards = deckResponse.cards.map { card ->
            val progress = progresses[card.id]
            when (card) {
                is CardDTO.WordCardDTO -> card.toLocal().withProgress(progress)
                is CardDTO.KanaCardDTO -> card.toLocalDTO(kanaCards).withProgress(progress)
                is CardDTO.KanjiCardDTO -> card.toLocalDTO(kanaCards).withProgress(progress)
                is CardDTO.PhraseCardDTO -> card.toLocalDTO(requiredWords).withProgress(progress)
            }
        }

        return deckResponse.toLocal(deck.name, cards)
    }

    private fun getRequiredCards(
        requiredDecks: List<DeckId>,
    ): List<CardDTO> = requiredDecks
        .mapNotNull { deckRepository.getDeckFromCache(it) }
        .flatMap { it.cards }
}