package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted

@Throws(IllegalStateException::class)
fun List<CardsPersisted>.convertCardsToDTO(deckId: String): DeckDTO {
    val cardDTOs = this.map { card ->
        when (card.type) {
            CardDTO.CARD_TYPE_WORD -> CardDTO.WordCardDTO(
                id = card.id,
                character = card.character,
                transcription = card.transcription,
                translation = card.translation ?: "",
                additionalInfo = card.additional_info,
                speechPart = card.speech_part ?: error("Speech part must be not null")
            )

            CardDTO.CARD_TYPE_PHRASE -> CardDTO.PhraseCardDTO(
                id = card.id,
                character = card.character,
                transcription = card.transcription,
                translation = card.translation ?: "",
                additionalInfo = card.additional_info,
                words = card.words ?: listOf()
            )

            CardDTO.CARD_TYPE_KANA -> CardDTO.KanaCardDTO(
                id = card.id,
                character = card.character,
                transcription = card.transcription,
                alphabet = card.alphabet ?: error("Alphabet must be not null"),
                mirror = card.mirror ?: error("Mirror kana must be not null")
            )

            CardDTO.CARD_TYPE_KANJI -> {
                val reading = CardDTO.KanjiCardDTO.ReadingDTO(
                    on = card.reading_on ?: listOf(),
                    kun = card.reading_kun ?: listOf()
                )
                CardDTO.KanjiCardDTO(
                    id = card.id,
                    character = card.character,
                    transcription = card.transcription,
                    reading = reading,
                    translation = card.translation,
                    additionalInfo = card.additional_info,
                    speechPart = card.speech_part ?: error("Speech part must be not null")
                )
            }

            else -> throw IllegalArgumentException("Unknown card type: ${card.type}")
        }
    }
    return DeckDTO(
        id = deckId,
        cards = cardDTOs,
        version = this.firstOrNull()?.version?.toInt() ?: 0
    )
}