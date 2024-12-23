package com.gaoyun.yanyou_kototomo.ui.player

import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.CardProgress
import com.gaoyun.yanyou_kototomo.data.local.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
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

    fun startPlayer(
        learningLanguageId: LanguageId,
        sourceLanguageId: LanguageId,
        courseId: CourseId,
        deckId: DeckId,
    ) = viewModelScope.launch {
        val course = getCoursesRoot.getCourseDecks(courseId)
        course.decks.find { it.id == deckId }?.let { deckInCourse ->
            val result = getDeck.getDeck(
                learningLanguage = learningLanguageId,
                sourceLanguage = sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )
            deckState.value = result.copy(cards = result.cards.shuffled())
            nextCard()
        }
    }

    fun nextCard() = viewModelScope.launch {
        closeCard()
        delay(300)
        val currentCardIndex = deckState.value?.cards?.indexOf(viewState.value?.card) ?: -1
        val newCardIndex = currentCardIndex + 1
        val deck = deckState.value ?: return@launch
        val card = deck.cards.getOrNull(newCardIndex) ?: return@launch
        viewState.value = PlayerCardViewState(
            card = card,
            isLast = newCardIndex == deck.cards.lastIndex,
            possibleAnswers = getPossibleAnswersFor(card.card, deck)
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
    val isLast: Boolean,
    val possibleAnswers: List<String>,
    val answerOpened: Boolean = false,
)