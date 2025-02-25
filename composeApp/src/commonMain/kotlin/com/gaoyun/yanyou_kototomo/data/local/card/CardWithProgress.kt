package com.gaoyun.yanyou_kototomo.data.local.card

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId

sealed interface CardWithProgress<T : Card> {
    val card: T
    val progress: CardProgress?

    data class Base<T : Card>(
        override val card: T,
        override val progress: CardProgress?,
    ) : CardWithProgress<T>

    data class WithDeckInfo<T : Card>(
        override val card: T,
        override val progress: CardProgress?,
        val deckId: DeckId,
        val deckName: String,
        val learningLanguageId: LanguageId,
    ) : CardWithProgress<T>

    fun isCompleted(): Boolean = this.progress?.completed == true
    fun hasProgress(): Boolean = this.progress?.hasProgress() == true

    fun withoutProgress() = when (this) {
        is Base -> this.copy(card, null)
        is WithDeckInfo -> this.copy(card, null, deckId, deckName)
    }

    fun asCompleted() = when (this) {
        is Base -> this.copy(card, CardProgress.completedCard(card.id))
        is WithDeckInfo -> this.copy(card, CardProgress.completedCard(card.id), deckId, deckName)
    }

    fun withDeckInfo(deckId: DeckId, deckName: String, learningLanguageId: LanguageId) =
        WithDeckInfo(card, progress, deckId, deckName, learningLanguageId)

    fun base() = Base(card, progress)

    fun countForReviewAndNotPausedIds(pausedCardIds: Collection<String>): Boolean {
        val isReviewCandidate = this.progress?.countForReview() != false
        val isPaused = this.card.id.identifier in pausedCardIds
        return isReviewCandidate && !isPaused
    }

    fun countForReviewAndNotPaused(pausedCards: Collection<CardWithProgress<*>>): Boolean {
        val pausedIds = pausedCards.mapTo(mutableSetOf()) { it.card.id.identifier }
        return countForReviewAndNotPausedIds(pausedIds)
    }
}

fun List<CardWithProgress<*>>.deckSorted() = this.sortedWith(compareBy { cardWithProgress ->
    when (cardWithProgress.card) {
        is Card.KanaCard -> 1
        is Card.KanjiCard -> 2
        is Card.WordCard -> 3
        is Card.PhraseCard -> 4
    }
})