package com.gaoyun.yanyou_kototomo.data.converters

import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO

fun DeckDTO.toLocal(name: String): Deck {
    return Deck(
        id = DeckId(this.id),
        name = name,
        cards = this.cards.map { it.toLocal() }
    )
}

fun DeckDTO.toLocalWithKana(
    name: String,
    kanaCards: List<CardDTO.KanaCardDTO>
): Deck {
    return Deck(
        id = DeckId(this.id),
        name = name,
        cards = this.cards.map { it.toLocal(kanaCards = kanaCards) }
    )
}

fun DeckDTO.toLocalWithWords(
    name: String,
    wordsCards: List<CardDTO.WordCardDTO>,
): Deck {
    return Deck(
        id = DeckId(this.id),
        name = name,
        cards = this.cards.map { it.toLocal(wordsCards = wordsCards) }
    )
}

fun DeckDTO.toLocalWithKanaAndWords(
    name: String,
    kanaCards: List<CardDTO.KanaCardDTO>,
    wordsCards: List<CardDTO.WordCardDTO>,
): Deck {
    return Deck(
        id = DeckId(this.id),
        name = name,
        cards = this.cards.map { it.toLocal(wordsCards = wordsCards, kanaCards = kanaCards) }
    )
}