package com.gaoyun.yanyou_kototomo.data.local.card

import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.util.localDateNow
import kotlinx.datetime.LocalDate

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

//Count for review
internal fun CardProgress?.countForReview(): Boolean {
    return when {
        // Consider for review if progress isn't tracked yet
        this == null -> true
        // Exclude if card is already completed
        completed -> false
        // Include if next review is due today or earlier
        else -> nextReview == null || nextReview <= localDateNow()
    }
}