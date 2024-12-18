package com.gaoyun.yanyou_kototomo.ui.player

import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class DeckPlayerViewModel(
    private val getDeck: GetDeck,
    private val getCoursesRoot: GetCoursesRoot,
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

    fun nextCard() {
        val currentCardIndex = deckState.value?.cards?.indexOf(viewState.value?.card) ?: -1
        val newCardIndex = currentCardIndex + 1
        viewState.value = PlayerCardViewState(
            card = deckState.value?.cards?.getOrNull(newCardIndex),
            isLast = newCardIndex == deckState.value?.cards?.lastIndex
        )
    }

    fun openCard() {
        viewState.value = viewState.value?.copy(answerOpened = true)
    }
}

data class PlayerCardViewState(
    val card: Card?,
    val isLast: Boolean,
    val answerOpened: Boolean = false,
)