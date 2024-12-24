package com.gaoyun.yanyou_kototomo.ui.deck_overview

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.DeckSettings
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.countForReview
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class DeckOverviewViewModel(
    private val getDeck: GetDeck,
    private val getCoursesRoot: GetCoursesRoot,
    private val deckSettingsInteractor: DeckSettingsInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<DeckOverviewState?>(null)

    fun getDeck(
        learningLanguageId: LanguageId,
        sourceLanguageId: LanguageId,
        courseId: CourseId,
        deckId: DeckId,
    ) = viewModelScope.launch {
        val course = getCoursesRoot.getCourseDecks(courseId)
        course.decks.find { it.id == deckId }?.let { deckInCourse ->
            val deck = getDeck.getDeck(
                learningLanguage = learningLanguageId,
                sourceLanguage = sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )
            val settings = deckSettingsInteractor.getDeckSettings(deckId) ?: DeckSettings.DEFAULT(deckId)
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
}

data class DeckOverviewState(
    val deck: Deck,
    val settings: DeckSettings,
    val cardsDueToReview: Int,
)