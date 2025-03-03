package com.gaoyun.yanyou_kototomo.data.ui_state

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.course.CourseWithInfo
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo

data class PersonalSpaceState(
    val bookmarks: List<DeckWithCourseInfo>,
    val learningCourse: CourseWithInfo?,
    val learningDecks: List<DeckId>,
    val settingsList: List<DeckSettings>,
    val deckSplits: List<DeckSplitResult.WithDeckInfo>,
    val cardsDueToReview: List<CardWithProgress.WithDeckInfo<*>>,
    val cardsToReview: List<CardWithProgress.WithDeckInfo<*>>,
    val newCards: List<CardWithProgress.WithDeckInfo<*>>,
    val pausedCards: List<CardWithProgress.WithDeckInfo<*>>,
    val completedCards: List<CardWithProgress.WithDeckInfo<*>>,
    val showToReviewCards: Boolean,
    val showNewCards: Boolean,
    val showPausedCards: Boolean,
    val showCompletedCards: Boolean,
) {
    fun hasCardsForProgressStatus(): Boolean {
        return newCards.isNotEmpty() || cardsToReview.isNotEmpty() || completedCards.isNotEmpty() || pausedCards.isNotEmpty()
    }
    fun newOrReviewCards(): Int {
        return newCards.size + cardsToReview.size
    }
}
