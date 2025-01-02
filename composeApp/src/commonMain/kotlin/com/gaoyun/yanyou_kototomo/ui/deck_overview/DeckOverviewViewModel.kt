package com.gaoyun.yanyou_kototomo.ui.deck_overview

import com.gaoyun.yanyou_kototomo.data.local.card.countForReview
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class DeckOverviewViewModel(
    private val getDeck: GetDeck,
    private val getCoursesRoot: GetCoursesRoot,
    private val deckSettingsInteractor: DeckSettingsInteractor,
    private val cardProgressUpdater: CardProgressUpdater,
    private val bookmarksInteractor: BookmarksInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<DeckOverviewState?>(null)
    val bookmarksState = MutableStateFlow(mutableListOf<CourseDeck>())
    val bookmarkState = MutableStateFlow<CourseDeck?>(null)

    fun getDeck(args: DeckScreenArgs) = viewModelScope.launch {
        val course = getCoursesRoot.getCourseDecks(args.courseId)
        course.decks.find { it.id == args.deckId }?.let { deckInCourse ->
            getDeck.getDeck(
                learningLanguage = args.learningLanguageId,
                sourceLanguage = args.sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )?.let { deck ->
                val settings = deckSettingsInteractor.getDeckSettings(args.deckId) ?: DeckSettings.DEFAULT(args.deckId)
                val cardsDueToReview = deck.cards.count { it.progress.countForReview() }

                val bookmarks = bookmarksInteractor.getBookmarkedDecks()
                bookmarksState.value = bookmarks.toMutableList()
                bookmarkState.value = bookmarks.find { it.id == deck.id }

                viewState.value = DeckOverviewState(
                    deck = deck,
                    settings = settings,
                    cardsDueToReview = cardsDueToReview,
                    isBookmarked = bookmarkState.value != null,
                    isCurrentlyLearned = bookmarksInteractor.getLearningDeck()?.id == deck.id
                )
            }
        }
    }

    fun updateTranslationSettings(show: Boolean) = viewModelScope.launch {
        viewState.value?.let { viewStateSafe ->
            val newSettings = viewStateSafe.settings.copy(showTranslation = show)
            deckSettingsInteractor.updateDeckSettings(newSettings)
            viewState.value = viewStateSafe.copy(settings = newSettings)
        }
    }

    fun updateTranscriptionSettings(show: Boolean) = viewModelScope.launch {
        viewState.value?.let { viewStateSafe ->
            val newSettings = viewStateSafe.settings.copy(showTranscription = show)
            deckSettingsInteractor.updateDeckSettings(newSettings)
            viewState.value = viewStateSafe.copy(settings = newSettings)
        }
    }

    fun updateReadingSettings(show: Boolean) = viewModelScope.launch {
        viewState.value?.let { viewStateSafe ->
            val newSettings = viewStateSafe.settings.copy(showReading = show)
            deckSettingsInteractor.updateDeckSettings(newSettings)
            viewState.value = viewStateSafe.copy(settings = newSettings)
        }
    }

    fun resetDeck(args: DeckScreenArgs) = viewModelScope.launch {
        viewState.value?.deck?.id?.let { cardProgressUpdater.resetDeck(it) }
        getDeck(args)
    }

    fun updateBookmarkedState(bookmarked: Boolean) = viewModelScope.launch {
        if (bookmarked) {
            viewState.value?.deck?.id?.let { bookmarksInteractor.addDeck(it, bookmarksState.value) }
        } else {
            bookmarksState.value.remove(bookmarkState.value)
            bookmarksInteractor.saveBookmarkedDecks(bookmarksState.value)
        }
        viewState.value = viewState.value?.copy(isBookmarked = bookmarked)
    }

    fun updateLearnedState(learned: Boolean) = viewModelScope.launch {
        if (learned) {
            viewState.value?.deck?.id?.let { bookmarksInteractor.setLearningDeckId(it) }
        } else {
            bookmarksInteractor.setLearningDeckId(null)
        }
        viewState.value = viewState.value?.copy(isCurrentlyLearned = learned)
    }
}

data class DeckOverviewState(
    val deck: Deck,
    val settings: DeckSettings,
    val cardsDueToReview: Int,
    val isCurrentlyLearned: Boolean,
    val isBookmarked: Boolean,
)