package com.gaoyun.yanyou_kototomo.data.local.card

import com.gaoyun.yanyou_kototomo.util.localDateNow
import kotlinx.datetime.LocalDate

data class CardWithProgress<T : Card>(
    val card: T,
    val progress: CardProgress?,
)

data class CardProgress(
    val cardId: String,
    val lastReviewed: LocalDate,
    val interval: Int,
    val easeFactor: Float,
    val nextReview: LocalDate,
)

fun CardProgress?.countForReview(): Boolean = this == null || this.nextReview == localDateNow()
fun List<CardWithProgress<*>>.deckSorted() = this.sortedWith(compareBy { cardWithProgress ->
    when (cardWithProgress.card) {
        is Card.KanaCard -> 1
        is Card.KanjiCard -> 2
        is Card.WordCard -> 3
        is Card.PhraseCard -> 4
    }
})