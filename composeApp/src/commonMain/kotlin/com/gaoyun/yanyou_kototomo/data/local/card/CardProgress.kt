package com.gaoyun.yanyou_kototomo.data.local.card

import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.util.localDateNow
import kotlinx.datetime.LocalDate

data class CardWithProgress<T : Card>(
    val card: T,
    val progress: CardProgress?,
)

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