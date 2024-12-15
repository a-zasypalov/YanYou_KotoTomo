package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted

fun CardsPersisted.toCardDTO(): CardDTO {
    return when (type) {
        "word" -> CardDTO.WordCardDTO(
            id = id,
            character = character,
            transcription = transcription,
            translation = translation!!,
            additionalInfo = additional_info,
            speechPart = speech_part!!
        )

        "phrase" -> CardDTO.PhraseCardDTO(
            id = id,
            character = character,
            transcription = transcription,
            translation = translation!!,
            additionalInfo = additional_info,
            words = words!!
        )

        "kana" -> CardDTO.KanaCardDTO(
            id = id,
            character = character,
            transcription = transcription,
            alphabet = alphabet!!,
            mirror = mirror!!
        )

        "kanji" -> CardDTO.KanjiCardDTO(
            id = id,
            character = character,
            transcription = transcription,
            reading = CardDTO.KanjiCardDTO.ReadingDTO(
                on = reading_on!!,
                kun = reading_kun!!
            ),
            translation = translation,
            additionalInfo = additional_info,
            speechPart = speech_part!!
        )

        else -> throw IllegalArgumentException("Unknown card type: $type")
    }
}