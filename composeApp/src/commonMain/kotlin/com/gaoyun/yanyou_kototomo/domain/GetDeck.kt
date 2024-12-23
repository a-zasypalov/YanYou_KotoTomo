package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.withProgress
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CardProgressRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository

class GetDeck(
    private val deckRepository: DeckRepository,
    private val cardProgressRepository: CardProgressRepository,
) {

    suspend fun getDeck(
        learningLanguage: LanguageId,
        sourceLanguage: LanguageId,
        deck: CourseDeck,
        requiredDecks: List<DeckId> = listOf(),
    ): Deck {
        val deckResponse = deckRepository.getDeck(learningLanguage, sourceLanguage, deck)
        val deckWords = deckResponse.cards.filterIsInstance<CardDTO.WordCardDTO>()
        val deckKana = deckResponse.cards.filterIsInstance<CardDTO.KanaCardDTO>()

        val requiredCards = getRequiredCards(learningLanguage, sourceLanguage, requiredDecks)
        val requiredWords = deckWords + requiredCards.filterIsInstance<CardDTO.WordCardDTO>()
        val kanaCards = deckKana + requiredCards.filterIsInstance<CardDTO.KanaCardDTO>()

        val progresses = cardProgressRepository.getCardProgressFor(deck.id).associateBy { it.cardId }

        val cards = deckResponse.cards.map { card ->
            val progress = progresses[card.id]
            when (card) {
                is CardDTO.WordCardDTO -> card.toLocal().withProgress(progress)
                is CardDTO.KanaCardDTO -> card.toLocal(kanaCards).withProgress(progress)
                is CardDTO.KanjiCardDTO -> card.toLocal(kanaCards).withProgress(progress)
                is CardDTO.PhraseCardDTO -> card.toLocal(requiredWords).withProgress(progress)
            }
        }

        return deckResponse.toLocal(deck.name, cards)
    }

    private suspend fun getRequiredCards(
        learningLanguage: LanguageId,
        sourceLanguage: LanguageId,
        requiredDecks: List<DeckId>,
    ): List<CardDTO> = requiredDecks
        .map { deckRepository.getDeck(learningLanguage, sourceLanguage, it) }
        .flatMap { it.cards }
}