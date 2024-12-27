package com.gaoyun.yanyou_kototomo.ui.deck_overview

import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckSettings
import com.gaoyun.yanyou_kototomo.data.local.card.countForReview
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
) : BaseViewModel() {

    override val viewState = MutableStateFlow<DeckOverviewState?>(null)

    fun getDeck(args: DeckScreenArgs) = viewModelScope.launch {
        val course = getCoursesRoot.getCourseDecks(args.courseId)
        course.decks.find { it.id == args.deckId }?.let { deckInCourse ->
            val deck = getDeck.getDeck(
                learningLanguage = args.learningLanguageId,
                sourceLanguage = args.sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )
            val settings = deckSettingsInteractor.getDeckSettings(args.deckId) ?: DeckSettings.DEFAULT(args.deckId)
            val cardsDueToReview = deck.cards.count { it.progress.countForReview() }

            viewState.value = DeckOverviewState(
                deck = deck,
                settings = settings,
                cardsDueToReview = cardsDueToReview
            )
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
}

data class DeckOverviewState(
    val deck: Deck,
    val settings: DeckSettings,
    val cardsDueToReview: Int,
)