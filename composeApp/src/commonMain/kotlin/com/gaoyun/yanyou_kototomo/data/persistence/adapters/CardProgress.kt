package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress
import kotlinx.datetime.LocalDate

fun com.gaoyun.yanyoukototomo.data.persistence.CardProgress.toLocal(): CardProgress {
    return CardProgress(
        cardId = this.card_id,
        lastReviewed = LocalDate.parse(this.lastReviewed),
        interval = this.interval.toInt(),
        easeFactor = this.easeFactor,
        nextReview = LocalDate.parse(this.nextReview),
    )
}