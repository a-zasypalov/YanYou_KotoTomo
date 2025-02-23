package com.gaoyun.yanyou_kototomo.data.local.card

import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.util.localDateNow
import kotlinx.datetime.LocalDate

sealed interface CardWithProgress<T : Card> {
    val card: T
    val progress: CardProgress?

    fun withDeckInfo(deckId: DeckId, deckName: String) = WithDeckInfo(card, progress, deckId, deckName)
    fun base() = Base(card, progress)
    fun withoutProgress() = when (this) {
        is Base -> this.copy(card, null)
        is WithDeckInfo -> this.copy(card, null, deckId, deckName)
    }

    fun asCompleted() = when (this) {
        is Base -> this.copy(card, CardProgress.completedCard(card.id))
        is WithDeckInfo -> this.copy(card, CardProgress.completedCard(card.id), deckId, deckName)
    }

    data class Base<T : Card>(
        override val card: T,
        override val progress: CardProgress?,
    ) : CardWithProgress<T>

    data class WithDeckInfo<T : Card>(
        override val card: T,
        override val progress: CardProgress?,
        val deckId: DeckId,
        val deckName: String,
    ) : CardWithProgress<T>
}

data class CardProgress(
    val cardId: String,
    val lastReviewed: LocalDate?,
    val interval: Int?,
    val easeFactor: Float?,
    val nextReview: LocalDate?,
    val completed: Boolean,
) {
    companion object {
        fun completedCard(cardId: CardId) = CardProgress(cardId.identifier, null, null, null, null, true)
    }

}

fun CardProgress?.hasProgress(): Boolean = this?.lastReviewed != null
fun CardWithProgress<*>.hasProgress(): Boolean = this.progress?.hasProgress() == true
fun CardWithProgress<*>.completed(): Boolean = this.progress?.completed == true

fun List<CardWithProgress<*>>.deckSorted() = this.sortedWith(compareBy { cardWithProgress ->
    when (cardWithProgress.card) {
        is Card.KanaCard -> 1
        is Card.KanjiCard -> 2
        is Card.WordCard -> 3
        is Card.PhraseCard -> 4
    }
})

//Count for review
private fun CardProgress?.countForReview(): Boolean {
    return when {
        // Consider for review if progress isn't tracked yet
        this == null -> true
        // Exclude if card is already completed
        completed -> false
        // Include if next review is due today or earlier
        else -> nextReview == null || nextReview <= localDateNow()
    }
}

fun CardWithProgress<*>.countForReviewAndNotPausedIds(pausedCardIds: Collection<String>): Boolean {
    val isReviewCandidate = this.progress?.countForReview() != false
    val isPaused = this.card.id.identifier in pausedCardIds
    return isReviewCandidate && !isPaused
}

fun CardWithProgress<*>.countForReviewAndNotPaused(pausedCards: Collection<CardWithProgress<*>>): Boolean {
    val pausedIds = pausedCards.mapTo(mutableSetOf()) { it.card.id.identifier }
    return countForReviewAndNotPausedIds(pausedIds)
}