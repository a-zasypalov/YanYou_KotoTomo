package com.gaoyun.yanyou_kototomo.data.converters

import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO

fun DeckDTO.toLocal(name: String, cards: List<Card>): Deck {
    return Deck(
        id = DeckId(this.id),
        name = name,
        cards = cards
    )
}