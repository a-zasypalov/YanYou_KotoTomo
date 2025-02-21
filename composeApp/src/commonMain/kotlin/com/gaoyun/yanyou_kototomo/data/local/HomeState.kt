package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo

data class HomeState(
    val bookmarks: List<DeckWithCourseInfo>,
    val recentlyReviewed: List<Pair<LanguageId, CardWithProgress<Card>>>,
)