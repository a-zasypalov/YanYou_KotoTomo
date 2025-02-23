package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.card.countForReviewAndNotPaused
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState

class PersonalSpaceInteractor(
    private val getUserSavedDecks: GetUserSavedDecks,
    private val deckSettingsInteractor: DeckSettingsInteractor,
) {

    suspend fun getPersonalSpaceState(): PersonalSpaceState {
        val learningDecks = getUserSavedDecks.getLearnedDecks()
        val settingsList = deckSettingsInteractor.getAllDeckSettings()
        val deckSplitResults = learningDecks.mapNotNull { deckWithCourse ->
            val settings = settingsList.find { it.deckId == deckWithCourse.deck.id } ?: return@mapNotNull null
            val cardsWithDeckInfo = deckWithCourse.deck.cards.map { it.withDeckInfo(deckWithCourse.deck.id, deckWithCourse.deck.name) }
            SplitDeckToNewReviewPaused.splitDeckToPersonalState(cardsWithDeckInfo, settings)
        }

        val allNewCards = deckSplitResults.flatMap { it.newCards }
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