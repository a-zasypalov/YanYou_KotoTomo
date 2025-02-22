package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.data.local.card.countForReviewAndNotPaused

class PersonalSpaceInteractor(
    private val getUserSavedDecks: GetUserSavedDecks,
    private val deckSettingsInteractor: DeckSettingsInteractor,
) {

    suspend fun getPersonalSpaceState(): PersonalSpaceState {
        val learningDecks = getUserSavedDecks.getLearnedDecks()
        val settingsList = deckSettingsInteractor.getAllDeckSettings()
        val deckSplitResults = learningDecks.mapNotNull { deckWithCourse ->
            val settings = settingsList.find { it.deckId == deckWithCourse.deck.id } ?: return@mapNotNull null
            SplitDeckToNewReviewPaused.splitDeckToNewReviewPaused(deckWithCourse.deck.cards, settings)
        }

        val allNewCards = deckSplitResults.map { it.newCards }
        val allPausedCards = deckSplitResults.flatMap { it.pausedCards }
        val allCompletedCards = deckSplitResults.flatMap { it.completedCards }
        val cardsToReview = deckSplitResults.flatMap { it.cardsToReview }
        val cardsDueToReview = cardsToReview.filter { it.countForReviewAndNotPaused(allPausedCards) }

        return PersonalSpaceState(
            learningDecks = learningDecks,
            settingsList = settingsList,
            deckSplits = deckSplitResults,
            cardsDueToReview = cardsDueToReview,
            cardsToReview = cardsToReview,
            newCards = allNewCards,
            pausedCards = allPausedCards,
            completedCards = allCompletedCards
        )
    }
}