package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.HOME_SCREEN_HIDDEN_SECTIONS
import com.gaoyun.yanyou_kototomo.data.ui_state.HomeScreenSection
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState

class HomeScreenInteractor(
    private val getUserSavedDecks: GetUserSavedDecks,
    private val deckSettingsInteractor: DeckSettingsInteractor,
    private val preferences: Preferences,
) {

    suspend fun getPersonalSpaceState(): PersonalSpaceState {
        val bookmarks = getUserSavedDecks.getBookmarks()
        val learningDecks = getUserSavedDecks.getLearnedDecks()
        val settingsList = deckSettingsInteractor.getAllDeckSettings()
        val deckSplitResults = learningDecks.map { deckWithCourse ->
            val settings = settingsList.find { it.deckId == deckWithCourse.deck.id } ?: DeckSettings.DEFAULT(deckWithCourse.deck.id)
            val cardsWithDeckInfo = deckWithCourse.deck.cards.map {
                it.withDeckInfo(
                    deckWithCourse.deck.id,
                    deckWithCourse.deck.name,
                    deckWithCourse.info.learningLanguageId
                )
            }
            SplitDeckToNewReviewPaused.splitDeckToPersonalState(cardsWithDeckInfo, settings)
        }

        val allNewCards = deckSplitResults.flatMap { it.newCards }
        val allPausedCards = deckSplitResults.flatMap { it.pausedCards }
        val allCompletedCards = deckSplitResults.flatMap { it.completedCards }
        val cardsToReview = deckSplitResults.flatMap { it.cardsToReview }
        val cardsDueToReview = cardsToReview.filter { it.countForReviewAndNotPaused(allPausedCards) }

        val hiddenSections = preferences.getStringSet(HOME_SCREEN_HIDDEN_SECTIONS).mapNotNull {
            try {
                HomeScreenSection.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        return PersonalSpaceState(
            bookmarks = bookmarks,
            learningDecks = learningDecks,
            settingsList = settingsList,
            deckSplits = deckSplitResults,
            cardsDueToReview = cardsDueToReview,
            cardsToReview = cardsToReview,
            newCards = allNewCards,
            pausedCards = allPausedCards,
            completedCards = allCompletedCards,
            showToReviewCards = !hiddenSections.contains(HomeScreenSection.CardsToReview),
            showNewCards = !hiddenSections.contains(HomeScreenSection.NewCards),
            showPausedCards = !hiddenSections.contains(HomeScreenSection.PausedCards),
            showCompletedCards = !hiddenSections.contains(HomeScreenSection.CompletedCards),
        )
    }

    fun toggleSectionVisibility(section: HomeScreenSection, visible: Boolean) {
        preferences.getStringSet(HOME_SCREEN_HIDDEN_SECTIONS).toMutableSet().apply {
            if (visible) remove(section.name) else add(section.name)
        }.run { preferences.setStringSet(HOME_SCREEN_HIDDEN_SECTIONS, this) }
    }
}