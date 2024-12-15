package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.convertCardsToDTO
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi

class DeckRepository(
    private val api: DecksApi,
    private val db: YanYouKotoTomoDatabase
) {

    suspend fun getDeck(
        learningLanguage: LanguageId,
        sourceLanguage: LanguageId,
        deckId: DeckId
    ): DeckDTO {
        val cache = getDeckFromCache(deckId)
        return cache ?: api.getDeck(
            learningLanguage.identifier,
            sourceLanguage.identifier,
            deckId.identifier
        ).also {
            cacheDeck(it, deckId)
        }
    }

    private fun getDeckFromCache(deckId: DeckId): DeckDTO? = runCatching {
        println("Getting deck $deckId from cache")
        db.decksQueries.getCardsForDeck(deckId.identifier).executeAsList()
            .convertCardsToDTO(deckId = deckId.identifier)
            .takeIf { it.cards.isNotEmpty() }
    }.getOrNull()

    private fun cacheDeck(deckDTO: DeckDTO, deckId: DeckId) {
        println("Caching deck $deckId from API")
        // Clear existing cache for the deck
        db.decksQueries.clearDeckCache(deckId.identifier)

        // Cache deck data
        db.decksQueries.insertDeck(deckId.identifier)

        deckDTO.cards.forEach { card ->
            when (card) {
                is CardDTO.WordCardDTO -> {
                    db.decksQueries.insertCard(
                        type = CardDTO.CARD_TYPE_WORD,
                        id = card.id,
                        deck_id = deckId.identifier,
                        character = card.character,
                        transcription = card.transcription,
                        translation = card.translation,
                        additional_info = card.additionalInfo,
                        speech_part = card.speechPart,
                        words = null,
                        alphabet = null,
                        mirror = null,
                        reading_on = null,
                        reading_kun = null
                    )
                }

                is CardDTO.PhraseCardDTO -> {
                    db.decksQueries.insertCard(
                        type = CardDTO.CARD_TYPE_PHRASE,
                        id = card.id,
                        deck_id = deckId.identifier,
                        character = card.character,
                        transcription = card.transcription,
                        translation = card.translation,
                        additional_info = card.additionalInfo,
                        words = card.words,
                        speech_part = null,
                        alphabet = null,
                        mirror = null,
                        reading_on = null,
                        reading_kun = null
                    )
                }

                is CardDTO.KanaCardDTO -> {
                    db.decksQueries.insertCard(
                        type = CardDTO.CARD_TYPE_KANA,
                        id = card.id,
                        deck_id = deckId.identifier,
                        character = card.character,
                        transcription = card.transcription,
                        alphabet = card.alphabet,
                        mirror = card.mirror,
                        translation = null,
                        additional_info = null,
                        speech_part = null,
                        words = null,
                        reading_on = null,
                        reading_kun = null
                    )
                }

                is CardDTO.KanjiCardDTO -> {
                    db.decksQueries.insertCard(
                        type = CardDTO.CARD_TYPE_KANJI,
                        id = card.id,
                        deck_id = deckId.identifier,
                        character = card.character,
                        transcription = card.transcription,
                        translation = card.translation,
                        additional_info = card.additionalInfo,
                        speech_part = card.speechPart,
                        reading_on = card.reading.on,
                        reading_kun = card.reading.kun,
                        words = null,
                        alphabet = null,
                        mirror = null,
                    )
                }
            }
        }
    }
}