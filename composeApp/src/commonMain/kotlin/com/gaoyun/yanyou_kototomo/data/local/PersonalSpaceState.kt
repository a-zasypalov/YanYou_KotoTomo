package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo

data class PersonalSpaceState(
    val learningDecks: List<DeckWithCourseInfo>,
    val settingsList: List<DeckSettings>,
    val deckSplits: List<DeckSplitResult>,
    val cardsDueToReview: List<CardWithProgress<*>>,
    val cardsToReview: List<CardWithProgress<*>>,
    val newCards: List<DeckPart>,
    val pausedCards: List<CardWithProgress<*>>,
    val completedCards: List<CardWithProgress<*>>,
)
