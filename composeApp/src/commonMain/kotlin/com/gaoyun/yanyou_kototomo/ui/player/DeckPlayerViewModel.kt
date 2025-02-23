package com.gaoyun.yanyou_kototomo.ui.player

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.persistence.QuizCardResultPersisted
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
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
) : BaseViewModel() {

    override val viewState = MutableStateFlow<PlayerCardViewState?>(null)
    private val deckState = MutableStateFlow<Deck?>(null)
    private val quizResults = MutableStateFlow<MutableList<QuizCardResultPersisted>>(mutableListOf())
    private val quizStart = MutableStateFlow<LocalDateTime?>(null)
    private val playerMode = MutableStateFlow<PlayerMode?>(null)

    private var finishCallback: ((QuizSessionId?) -> Unit)? = null

    fun startPlayer(
        learningLanguageId: LanguageId,
        sourceLanguageId: LanguageId,
        courseId: CourseId,
        deckId: DeckId,
        playerMode: PlayerMode,
        finishCallback: (QuizSessionId?) -> Unit,
    ) = viewModelScope.launch {
        this@DeckPlayerViewModel.finishCallback = finishCallback
        this@DeckPlayerViewModel.playerMode.value = playerMode
        val course = getCoursesRoot.getCourseDecks(courseId)
        course.decks.find { it.id == deckId }?.let { deckInCourse ->
            getDeck.getDeck(
                learningLanguage = learningLanguageId,
                sourceLanguage = sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )?.let { result ->
                val settings = deckSettingsInteractor.getDeckSettings(deckId) ?: DeckSettings.DEFAULT(deckId)
                val filteredCards = result.cards.filterNot { settings.pausedCards.contains(it.card.id.identifier) }

                val cardForPlayer = when (playerMode) {
                    PlayerMode.SpacialRepetition -> filteredCards.filter { it.countForReviewAndNotPausedIds(settings.pausedCards) }
                        .shuffled()

                    PlayerMode.Quiz -> filteredCards.shuffled().also { quizStart.value = localDateTimeNow() }
                }
                deckState.value = result.copy(cards = cardForPlayer)
                nextCard()
            }
        }
    }

    fun nextCard() = viewModelScope.launch {
        val currentCardIndex = deckState.value?.cards?.indexOf(viewState.value?.card) ?: -1
        val newCardIndex = currentCardIndex + 1
        val totalNumOfCards = deckState.value?.cards?.count() ?: -1

        if (newCardIndex == totalNumOfCards) {
            finishPlayer()
            return@launch
        }

        closeCard()
        delay(300)

        val deck = deckState.value ?: return@launch
        val card = deck.cards.getOrNull(newCardIndex) ?: return@launch

        val intervals = if (playerMode.value == PlayerMode.SpacialRepetition) {
            spacedRepetitionCalculation.calculateNextIntervals(card.progress?.nextReview, card.progress?.easeFactor)
        } else null

        viewState.value = PlayerCardViewState(
            card = card,
            possibleAnswers = getPossibleAnswersFor(card.card, deck),
            cardNumOutOf = newCardIndex + 1 to totalNumOfCards,
            intervalsInDays = intervals
        )
    }

    private fun finishPlayer() {
        when (playerMode.value) {
            PlayerMode.Quiz -> viewModelScope.launch {
                val newSessionId = quizInteractor.addSession(quizStart.value ?: localDateTimeNow(), quizResults.value)
                finishCallback?.invoke(newSessionId)
            }

            else -> finishCallback?.invoke(null)
        }
    }

    fun repetitionAnswer(answer: RepetitionAnswer) = viewModelScope.launch {
        val deckId = deckState.value?.id ?: return@launch
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
        val currentCard = viewState.value?.card?.card ?: return
        val isAnswerCorrect = getAnswerFor(currentCard) == answer
        viewState.value = viewState.value?.copy(answerOpened = true, answerIsCorrect = isAnswerCorrect)
        quizResults.value.add(QuizCardResultPersisted(currentCard.id.identifier, isAnswerCorrect))
    }

    fun closeCard() {
        viewState.value = viewState.value?.copy(answerOpened = false, answerIsCorrect = null)
    }

    fun getPossibleAnswersFor(card: Card, deck: Deck): List<String> {
        val correctAnswer = getAnswerFor(card)
        val clearedDeck = deck.cards.map { it.card }.toMutableList().also { it.remove(card) }

        val possibleAnswers = when (card) {
            is Card.KanaCard -> clearedDeck.filter { it is Card.KanaCard }
            is Card.WordCard,
            is Card.PhraseCard,
            is Card.KanjiCard,
                -> clearedDeck.filterNot { it is Card.KanaCard }
        }

        val allAnswers = possibleAnswers.shuffled().take(3).map { getAnswerFor(it) } + correctAnswer
        return allAnswers.shuffled()
    }

    private fun getAnswerFor(card: Card): String = when (card) {
        is Card.PhraseCard -> card.translation
        is Card.WordCard -> card.translation
        is Card.KanjiCard -> card.translation
        is Card.KanaCard -> card.transcription
    }
}

