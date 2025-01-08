package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted

@Throws(IllegalStateException::class)
fun List<CardsPersisted>.convertCardsToDTO(deckId: String): DeckDTO {
    return DeckDTO(
        id = deckId,
        cards = convertCardsToDTO(),
        version = this.firstOrNull()?.version?.toInt() ?: 0
    )
}

fun List<CardsPersisted>.convertCardsToDTO(): List<CardDTO> = map { it.toCardsDTO() }

fun CardsPersisted.toCardsDTO(): CardDTO = when (type) {
    CardDTO.CARD_TYPE_WORD -> CardDTO.WordCardDTO(
        id = id,
        character = character,
        transcription = transcription ?: "",
        translation = translation ?: "",
        additionalInfo = additional_info,
        speechPart = speech_part ?: error("Speech part must be not null")
    )

    CardDTO.CARD_TYPE_PHRASE -> CardDTO.PhraseCardDTO(
        id = id,
        character = character,
        transcription = transcription ?: "",
        translation = translation ?: "",
        additionalInfo = additional_info,
        words = words ?: listOf()
    )

    CardDTO.CARD_TYPE_KANA -> CardDTO.KanaCardDTO(
        id = id,
        character = character,
        transcription = transcription ?: "",
        alphabet = alphabet ?: error("Alphabet must be not null"),
        mirror = mirror ?: error("Mirror kana must be not null")
    )

    CardDTO.CARD_TYPE_KANJI -> {
        val reading = CardDTO.KanjiCardDTO.ReadingDTO(
            on = reading_on ?: listOf(),
            kun = reading_kun ?: listOf()
        )
        CardDTO.KanjiCardDTO(
            id = id,
            character = character,
            reading = reading,
            translation = translation ?: error("Translation must be not null"),
            additionalInfo = additional_info,
            speechPart = speech_part ?: error("Speech part must be not null")
        )
    }

    else -> throw IllegalArgumentException("Unknown card type: ${type}")
}
