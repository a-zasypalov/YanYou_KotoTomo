package com.gaoyun.yanyou_kototomo.ui.player

import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.countForReview
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import com.gaoyun.yanyou_kototomo.util.localDateNow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class DeckPlayerViewModel(
    private val getDeck: GetDeck,
    private val getCoursesRoot: GetCoursesRoot,
    private val spacedRepetitionCalculation: SpacedRepetitionCalculation,
    private val cardProgressUpdater: CardProgressUpdater,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<PlayerCardViewState?>(null)
    private val deckState = MutableStateFlow<Deck?>(null)
    private var finishCallback: (() -> Unit)? = null

    fun startPlayer(
        learningLanguageId: LanguageId,
        sourceLanguageId: LanguageId,
        courseId: CourseId,
        deckId: DeckId,
        playerMode: PlayerMode,
        finishCallback: () -> Unit,
    ) = viewModelScope.launch {
        this@DeckPlayerViewModel.finishCallback = finishCallback
        val course = getCoursesRoot.getCourseDecks(courseId)
        course.decks.find { it.id == deckId }?.let { deckInCourse ->
            val result = getDeck.getDeck(
                learningLanguage = learningLanguageId,
                sourceLanguage = sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )
            val cardForPlayer = when (playerMode) {
                PlayerMode.SpacialRepetition -> result.cards.filter { it.progress.countForReview() }.shuffled()
                PlayerMode.Quiz -> result.cards.shuffled()
            }
            deckState.value = result.copy(cards = cardForPlayer)
            nextCard()
        }
    }

    fun nextCard() = viewModelScope.launch {
        val currentCardIndex = deckState.value?.cards?.indexOf(viewState.value?.card) ?: -1
        val newCardIndex = currentCardIndex + 1
        val totalNumOfCards = deckState.value?.cards?.count() ?: -1

        if (newCardIndex == totalNumOfCards) {
            finishCallback?.invoke()
            return@launch
        }

        closeCard()
        delay(300)

        val deck = deckState.value ?: return@launch
        val card = deck.cards.getOrNull(newCardIndex) ?: return@launch
        viewState.value = PlayerCardViewState(
            card = card,
            possibleAnswers = getPossibleAnswersFor(card.card, deck),
            cardNumOutOf = newCardIndex + 1 to totalNumOfCards
        )
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
                interval = intervalDays
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
        viewState.value = viewState.value?.copy(answerOpened = true)
    }

    fun closeCard() {
        viewState.value = viewState.value?.copy(answerOpened = false)
    }

    fun getPossibleAnswersFor(card: Card, deck: Deck): List<String> {
        val correctAnswer = getAnswerFor(card)
        val clearedDeck = deck.cards.map { it.card }.toMutableList().also { it.remove(card) }
        val allAnswers = clearedDeck.shuffled().take(3).map { getAnswerFor(it) } + correctAnswer
        return allAnswers.shuffled()
    }

    private fun getAnswerFor(card: Card): String = when (card) {
        is Card.PhraseCard -> card.translation
        is Card.WordCard -> card.translation
        is Card.KanjiCard -> card.translation
        is Card.KanaCard -> card.transcription
    }
}

data class PlayerCardViewState(
    val card: CardWithProgress<*>?,
    val possibleAnswers: List<String>,
    val cardNumOutOf: Pair<Int, Int>,
    val answerOpened: Boolean = false,
)