package com.gaoyun.yanyou_kototomo.ui.player

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.persistence.QuizCardResultPersisted
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenArgs
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import com.gaoyun.yanyou_kototomo.util.localDateNow
import com.gaoyun.yanyou_kototomo.util.localDateTimeNow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class DeckPlayerViewModel(
    private val getDeck: GetDeck,
    private val getCoursesRoot: GetCoursesRoot,
    private val spacedRepetitionCalculation: SpacedRepetitionCalculation,
    private val cardProgressUpdater: CardProgressUpdater,
    private val quizInteractor: QuizInteractor,
    private val deckSettingsInteractor: DeckSettingsInteractor,
    private val bookmarksInteractor: BookmarksInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<PlayerCardViewState?>(null)
    private val cardsForPlayer = MutableStateFlow<List<Pair<DeckId, CardWithProgress<*>>>>(listOf())
    private val quizResults = MutableStateFlow<MutableList<QuizCardResultPersisted>>(mutableListOf())
    private val quizStart = MutableStateFlow<LocalDateTime?>(null)
    private val argsState = MutableStateFlow<PlayerScreenArgs?>(null)

    private var finishCallback: ((QuizSessionId?) -> Unit)? = null

    fun startPlayer(
        args: PlayerScreenArgs,
        finishCallback: (QuizSessionId?) -> Unit,
    ) = viewModelScope.launch {
        argsState.value = args
        this@DeckPlayerViewModel.finishCallback = finishCallback
        val learningDeckIds = bookmarksInteractor.getLearningDecks().map { it.id }
        val course = getCoursesRoot.getCourse(args.courseId)
        course.decks.filter { args.deckIds.contains(it.id) }
            .mapNotNull { deckInCourse ->
                getDeck.getDeck(
                    learningLanguage = args.learningLanguageId,
                    sourceLanguage = args.sourceLanguageId,
                    deck = deckInCourse,
                    requiredDecks = course.requiredDecks ?: listOf()
                )
            }
            .let { resultList ->
                val settings = args.deckIds
                    .map { deckSettingsInteractor.getDeckSettings(it) ?: DeckSettings.DEFAULT(it) }
                    .associate { it.deckId to it }

                val filteredCards = resultList
                    .filter { if (args is PlayerScreenArgs.MixedDeckReview) learningDeckIds.contains(it.id) else true }
                    .map { item -> item.cards.map { item.id to it } }
                    .flatten()
                    .filterNot {
                        val pausedCards = settings[it.first]?.pausedCards ?: listOf()
                        pausedCards.contains(it.second.card.id.identifier)
                    }

                val cardForPlayer = when (args) {
                    is PlayerScreenArgs.MixedDeckReview,
                    is PlayerScreenArgs.DeckReview,
                        -> filteredCards.filter {
                        it.second.countForReviewAndNotPausedIds(settings[it.first]?.pausedCards ?: listOf())
                    }

                    is PlayerScreenArgs.DeckQuiz -> filteredCards.also { quizStart.value = localDateTimeNow() }
                }

                cardsForPlayer.value = cardForPlayer.map { it.first to it.second }.shuffled()
                nextCard()
            }
    }

    fun nextCard() = viewModelScope.launch {
        val cards = cardsForPlayer.value.map { it.second }
        val currentCardIndex = cards.indexOf(viewState.value?.card)
        val newCardIndex = currentCardIndex + 1
        val totalNumOfCards = cardsForPlayer.value.count()

        if (newCardIndex == totalNumOfCards) {
            finishPlayer()
            return@launch
        }

        closeCard()
        delay(300)

        val card = cardsForPlayer.value.getOrNull(newCardIndex)?.second ?: return@launch

        val intervals = if (argsState.value is PlayerScreenArgs.SpacialRepetition) {
            spacedRepetitionCalculation.calculateNextIntervals(card.progress?.nextReview, card.progress?.easeFactor)
        } else null

        viewState.value = PlayerCardViewState(
            card = card,
            possibleAnswers = getPossibleAnswersFor(card.card, cards),
            cardNumOutOf = newCardIndex + 1 to totalNumOfCards,
            intervalsInDays = intervals
        )
    }

    private fun finishPlayer() {
        when (argsState.value) {
            is PlayerScreenArgs.DeckQuiz -> viewModelScope.launch {
                val newSessionId = quizInteractor.addSession(quizStart.value ?: localDateTimeNow(), quizResults.value)
                finishCallback?.invoke(newSessionId)
            }

            else -> finishCallback?.invoke(null)
        }
    }

    fun repetitionAnswer(answer: RepetitionAnswer, cardId: CardId) = viewModelScope.launch {
        val deckId = cardsForPlayer.value.find { it.second.card.id == cardId }?.first ?: return@launch
        val currentCard = viewState.value?.card ?: return@launch
        val reviewDate = localDateNow()
        val (nextReviewDate, newEaseFactor, intervalDays) = spacedRepetitionCalculation.calculateNextInterval(
            currentReviewDate = reviewDate,
            easeFactorInput = currentCard.progress?.easeFactor,
            reviewQuality = answer
        )
        cardProgressUpdater.updateCardProgress(
            deckId = deckId,
            cardProgress = CardProgress(
                cardId = currentCard.card.id.identifier,
                nextReview = nextReviewDate,
                easeFactor = newEaseFactor,
                lastReviewed = reviewDate,
                interval = intervalDays,
                completed = currentCard.isCompleted()
            )
        )
        nextCard()
    }

    fun openCard() {
        viewState.value = viewState.value?.copy(answerOpened = true)
    }

    fun answerCard(answer: String) {
        val isKanaCourse = argsState.value?.courseId?.isKanaCourse() == true
        val currentCard = viewState.value?.card?.card ?: return
        val isAnswerCorrect = getAnswerFor(currentCard, isKanaCourse) == answer
        viewState.value = viewState.value?.copy(answerOpened = true, answerIsCorrect = isAnswerCorrect)
        quizResults.value.add(QuizCardResultPersisted(currentCard.id.identifier, isAnswerCorrect))
    }

    fun closeCard() {
        viewState.value = viewState.value?.copy(answerOpened = false, answerIsCorrect = null)
    }

    fun getPossibleAnswersFor(card: Card, allCards: List<CardWithProgress<*>>): List<String> {
        val isKanaCourse = argsState.value?.courseId?.isKanaCourse() == true
        val correctAnswer = getAnswerFor(card, isKanaCourse)
        val clearedDeck = allCards.map { it.card }.toMutableList().also { it.remove(card) }

        val possibleAnswers = when (card) {
            is Card.KanaCard -> clearedDeck.filter { it is Card.KanaCard }
            is Card.WordCard,
            is Card.PhraseCard,
            is Card.KanjiCard,
                -> clearedDeck.filterNot { it is Card.KanaCard }
        }

        val allAnswers = possibleAnswers.shuffled().take(3).map { getAnswerFor(it, isKanaCourse) } + correctAnswer
        return allAnswers.shuffled()
    }

    private fun getAnswerFor(card: Card, isKanaCourse: Boolean): String = if (isKanaCourse) {
        card.transcription()
    } else when (card) {
        is Card.PhraseCard -> card.translation
        is Card.WordCard -> card.translation
        is Card.KanjiCard -> card.translation
        is Card.KanaCard -> card.transcription
    }
}

