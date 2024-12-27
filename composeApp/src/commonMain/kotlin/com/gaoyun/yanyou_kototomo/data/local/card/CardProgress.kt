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
    val easeFactor: Double,
    val nextReview: LocalDate,
)

fun CardProgress?.countForReview(): Boolean = this == null || this.nextReview == localDateNow()