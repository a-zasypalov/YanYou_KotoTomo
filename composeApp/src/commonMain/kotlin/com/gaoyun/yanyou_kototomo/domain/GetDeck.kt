package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.converters.toLocal
import com.gaoyun.yanyou_kototomo.data.local.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetDeck(private val repository: DeckRepository) {
    suspend operator fun invoke(
        learningLanguage: LanguageId,
        sourceLanguage: LanguageId,
        deck: CourseDeck,
        requiredDecks: List<DeckId> = listOf()
    ): Flow<Deck> {
        val deckResponse = repository.getDeck(learningLanguage, sourceLanguage, deck.id)
        val deckWords = deckResponse.cards.filterIsInstance<CardDTO.WordCardDTO>()
        val deckKana = deckResponse.cards.filterIsInstance<CardDTO.KanaCardDTO>()

        val requiredCards = getRequiredCards(learningLanguage, sourceLanguage, requiredDecks)
        val requiredWords = deckWords + requiredCards.filterIsInstance<CardDTO.WordCardDTO>()
        val kanaCards = deckKana + requiredCards.filterIsInstance<CardDTO.KanaCardDTO>()

        val cards = deckResponse.cards.map { card ->
            when (card) {
                is CardDTO.WordCardDTO -> card.toLocal()
                is CardDTO.KanaCardDTO -> card.toLocal(kanaCards)
                is CardDTO.KanjiCardDTO -> card.toLocal(kanaCards)
                is CardDTO.PhraseCardDTO -> card.toLocal(requiredWords)
            }
        }

        return flowOf(deckResponse.toLocal(deck.name, cards))
    }

    private suspend fun getRequiredCards(
        learningLanguage: LanguageId,
        sourceLanguage: LanguageId,
        requiredDecks: List<DeckId>,
    ): List<CardDTO> = requiredDecks
        .map { repository.getDeck(learningLanguage, sourceLanguage, it) }
        .flatMap { it.cards }
}