package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck

data class HomeState(
    val currentlyLearn: Pair<CourseId, Deck?>?,
    val bookmarks: List<Pair<CourseId, Deck>>,
    val recentlyReviewed: List<CardWithProgress<Card>>,
)