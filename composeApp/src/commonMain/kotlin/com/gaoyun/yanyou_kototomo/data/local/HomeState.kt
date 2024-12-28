package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck

data class HomeState(
    val currentlyLearn: Deck?,
    val bookmarks: List<Deck>,
    val recentlyReviewed: List<CardWithProgress<Card>>,
)