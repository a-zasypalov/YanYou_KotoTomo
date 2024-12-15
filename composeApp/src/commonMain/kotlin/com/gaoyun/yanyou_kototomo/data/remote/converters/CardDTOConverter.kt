package com.gaoyun.yanyou_kototomo.data.remote.converters

import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO

fun CardDTO.PhraseCardDTO.toLocal(availableWords: List<CardDTO.WordCardDTO>): Card {
    return Card.PhraseCard(
        id = CardId.PhraseCardId(this.id),
        front = this.character,
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
        front = this.character,
        transcription = this.transcription,
        translation = this.translation,
        additionalInfo = this.additionalInfo,
        speechPart = this.speechPart
    )
}

fun CardDTO.KanaCardDTO.toLocal(kanaCards: List<CardDTO.KanaCardDTO>): Card.KanaCard {
    return Card.KanaCard(
        id = CardId.AlphabetCardId(this.id),
        front = this.character,
        transcription = this.transcription,
        alphabet = this.alphabet.toAlphabet() ?: error("Wrong alphabet for card $id"),
        mirror = Card.KanaCard.Mirror(
            id = CardId.AlphabetCardId(this.mirror),
            front = kanaCards.find { it.id == this.mirror }?.character
                ?: error("No mirror kana for card $id")
        )
    )
}

fun CardDTO.KanjiCardDTO.toLocal(kanaCards: List<CardDTO.KanaCardDTO>): Card.KanjiCard {
    return Card.KanjiCard(
        id = CardId.WordCardId(this.id),
        front = this.character,
        transcription = this.transcription,
        reading = Card.KanjiCard.Reading(
            on = this.reading.on.mapNotNull { kanaId ->
                kanaCards.find { it.id == kanaId }?.toLocal(kanaCards)
            },
            kun = this.reading.kun.mapNotNull { kanaId ->
                kanaCards.find { it.id == kanaId }?.toLocal(kanaCards)
            },
        ),
        translation = this.translation,
        additionalInfo = this.additionalInfo,
        speechPart = this.speechPart
    )
}