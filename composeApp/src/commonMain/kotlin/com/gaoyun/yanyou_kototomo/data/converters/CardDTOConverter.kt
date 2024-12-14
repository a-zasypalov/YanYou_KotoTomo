package com.gaoyun.yanyou_kototomo.data.converters

import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO

fun CardDTO.toLocal(
    wordsCards: List<CardDTO.WordCardDTO> = listOf(),
    kanaCards: List<CardDTO.KanaCardDTO> = listOf()
): Card {
    return when (this) {
        is CardDTO.PhraseCardDTO -> toLocal(wordsCards)
        is CardDTO.WordCardDTO -> toLocal()
        is CardDTO.KanaCardDTO -> toLocal()
        is CardDTO.KanjiCardDTO -> toLocal(kanaCards)
    }
}

fun CardDTO.PhraseCardDTO.toLocal(availableWords: List<CardDTO.WordCardDTO>): Card {
    return Card.PhraseCard(
        id = CardId.PhraseCardId(this.id),
        front = this.front,
        transcription = this.transcription,
        translation = this.translation,
        additionalInfo = this.additionalInfo,
        words = this.words.mapNotNull { wordId ->
            availableWords.find { word -> word.id == wordId }?.toLocal()
        }
    )
}

fun CardDTO.WordCardDTO.toLocal(): Card.WordCard {
    return Card.WordCard(
        id = CardId.WordCardId(this.id),
        front = this.front,
        transcription = this.transcription,
        translation = this.translation,
        additionalInfo = this.additionalInfo,
        speechPart = this.speechPart
    )
}

fun CardDTO.KanaCardDTO.toLocal(): Card.KanaCard {
    return Card.KanaCard(
        id = CardId.AlphabetCardId(this.id),
        front = this.front,
        transcription = this.transcription,
        alphabet = this.alphabet,
        mirror = Card.KanaCard.Mirror(
            id = CardId.AlphabetCardId(this.mirror),
            front = this.front
        )
    )
}

fun CardDTO.KanjiCardDTO.toLocal(kanaCards: List<CardDTO.KanaCardDTO>): Card.KanjiCard {
    return Card.KanjiCard(
        id = CardId.WordCardId(this.id),
        front = this.front,
        transcription = this.transcription,
        reading = Card.KanjiCard.Reading(
            on = this.reading.on.mapNotNull { kanaId ->
                kanaCards.find { it.id == kanaId }?.toLocal()
            },
            kun = this.reading.kun.mapNotNull { kanaId ->
                kanaCards.find { it.id == kanaId }?.toLocal()
            },
        ),
        translation = this.translation,
        additionalInfo = this.additionalInfo,
        speechPart = this.speechPart
    )
}