package com.gaoyun.yanyou_kototomo.ui.deck_overview

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.countForReview
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
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
import androidx.lifecycle.viewModelScope

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

                val bookmarks = bookmarksInteractor.getBookmarkedDecks()
                bookmarksState.value = bookmarks.toMutableList()
                bookmarkState.value = bookmarks.find { it.id == deck.id }

                val (newCards, cardsToReview, pausedCards) = splitDeckToNewReviewPaused(deck.cards, settings)
                val cardsDueToReview = deck.cards.count { it.progress.countForReview() && !pausedCards.contains(it) }

                viewState.value = DeckOverviewState(
                    deckId = deck.id,
                    deckName = deck.name,
                    allCards = deck.cards,
                    newCards = newCards,
                    cardsToReview = cardsToReview,
                    pausedCards = pausedCards,
                    settings = settings,
                    cardsDueToReview = cardsDueToReview,
                    isBookmarked = bookmarkState.value != null,
                    isCurrentlyLearned = bookmarksInteractor.getLearningDeck()?.id == deck.id
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun splitDeckToNewReviewPaused(
        cards: List<CardWithProgress<*>>,
        settings: DeckSettings,
    ): Triple<DeckPart, List<CardWithProgress<*>>, List<CardWithProgress<*>>> {
        val pausedCardIds = settings.pausedCards.toSet()
        val pausedCards = mutableListOf<CardWithProgress<*>>()
        val cardsToReview = mutableListOf<CardWithProgress<*>>()
        val newWords = mutableListOf<CardWithProgress<Card.WordCard>>()
        val newPhrases = mutableListOf<CardWithProgress<Card.PhraseCard>>()
        val newKanji = mutableListOf<CardWithProgress<Card.KanjiCard>>()
        val newKana = mutableListOf<CardWithProgress<Card.KanaCard>>()

        for (card in cards) {
            when {
                pausedCardIds.contains(card.card.id.identifier) -> pausedCards.add(card)
                card.progress != null && card.card !is Card.KanaCard -> cardsToReview.add(card)
                else -> when (card.card) {
                    is Card.WordCard -> newWords.add(card as CardWithProgress<Card.WordCard>)
                    is Card.PhraseCard -> newPhrases.add(card as CardWithProgress<Card.PhraseCard>)
                    is Card.KanjiCard -> newKanji.add(card as CardWithProgress<Card.KanjiCard>)
                    is Card.KanaCard -> newKana.add(card as CardWithProgress<Card.KanaCard>)
                }
            }
        }

        return Triple(DeckPart(newKanji, newWords, newPhrases, newKana), cardsToReview.sortedBy { it.progress?.nextReview }, pausedCards)
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
        viewState.value?.let { viewState ->
            cardProgressUpdater.resetDeck(viewState.deckId)
            deckSettingsInteractor.updateDeckSettings(viewState.settings.copy(pausedCards = setOf()))
        }
        getDeck(args)
    }

    fun pauseCard(card: CardWithProgress<*>, pause: Boolean) = viewModelScope.launch {
        viewState.value?.let { viewStateSafe ->
            val newPausedCards = if (pause) {
                viewStateSafe.settings.pausedCards.toList() + card.card.id.identifier
            } else {
                viewStateSafe.settings.pausedCards.toList() - card.card.id.identifier
            }

            val newSettings = viewStateSafe.settings.copy(pausedCards = newPausedCards.toSet())
            deckSettingsInteractor.updateDeckSettings(newSettings)

            val (newCards, cardsToReview, pausedCards) = splitDeckToNewReviewPaused(viewStateSafe.allCards, newSettings)
            viewState.value = viewStateSafe.copy(
                newCards = newCards,
                cardsToReview = cardsToReview,
                pausedCards = pausedCards,
                cardsDueToReview = viewStateSafe.allCards.count { it.progress.countForReview() && !pausedCards.contains(it) },
                settings = newSettings
            )
        }
    }

    fun updateBookmarkedState(bookmarked: Boolean) = viewModelScope.launch {
        if (bookmarked) {
            viewState.value?.deckId?.let { bookmarksInteractor.addDeck(it, bookmarksState.value) }
        } else {
            bookmarksState.value.remove(bookmarkState.value)
            bookmarksInteractor.saveBookmarkedDecks(bookmarksState.value)
        }
        viewState.value = viewState.value?.copy(isBookmarked = bookmarked)
    }

    fun updateLearnedState(learned: Boolean) = viewModelScope.launch {
        if (learned) {
            viewState.value?.deckId?.let { bookmarksInteractor.setLearningDeckId(it) }
        } else {
            bookmarksInteractor.setLearningDeckId(null)
        }
        viewState.value = viewState.value?.copy(isCurrentlyLearned = learned)
    }

    fun updateShowNewWords(show: Boolean) = updateDeckSettingsSectionSetting(DeckSettings.Sections.NewWords, show)
    fun updateShowNewPhrases(show: Boolean) = updateDeckSettingsSectionSetting(DeckSettings.Sections.NewPhrases, show)
    fun updateShowToReviewCards(show: Boolean) = updateDeckSettingsSectionSetting(DeckSettings.Sections.Review, show)
    fun updateShowPausedCards(show: Boolean) = updateDeckSettingsSectionSetting(DeckSettings.Sections.Paused, show)
    fun updateShowKanjiCards(show: Boolean) = updateDeckSettingsSectionSetting(DeckSettings.Sections.Kanji, show)

    fun updateDeckSettingsSectionSetting(section: DeckSettings.Sections, show: Boolean) = viewModelScope.launch {
        viewState.value?.let { viewStateSafe ->
            val newSettings = viewStateSafe.settings.copy(
                hiddenSections = viewStateSafe.settings.hiddenSections.toMutableSet()
                    .also { if (show) it.remove(section) else it.add(section) }
            )
            deckSettingsInteractor.updateDeckSettings(newSettings)
            viewState.value = viewStateSafe.copy(settings = newSettings)
        }
    }
}

data class DeckOverviewState(
    val deckId: DeckId,
    val deckName: String,
    val allCards: List<CardWithProgress<*>>,
    val newCards: DeckPart,
    val cardsToReview: List<CardWithProgress<*>>,
    val pausedCards: List<CardWithProgress<*>>,
    val settings: DeckSettings,
    val cardsDueToReview: Int,
    val isCurrentlyLearned: Boolean,
    val isBookmarked: Boolean,
)

data class DeckPart(
    val kanji: List<CardWithProgress<Card.KanjiCard>>,
    val words: List<CardWithProgress<Card.WordCard>>,
    val phrases: List<CardWithProgress<Card.PhraseCard>>,
    val kana: List<CardWithProgress<Card.KanaCard>>,
) {
    fun size() = kanji.size + words.size + phrases.size + kana.size
}