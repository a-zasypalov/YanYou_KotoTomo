package com.gaoyun.yanyou_kototomo.data.remote.converters

import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO

fun DeckDTO.toLocal(name: String, cards: List<CardWithProgress<*>>): Deck {
    return Deck(
        id = DeckId(this.id),
        name = name,
        cards = cards
    )
}