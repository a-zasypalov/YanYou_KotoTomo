package com.gaoyun.yanyou_kototomo.ui.player

import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress

data class PlayerCardViewState(
    val card: CardWithProgress<*>?,
    val possibleAnswers: List<String>,
    val cardNumOutOf: Pair<Int, Int>,
    val answerOpened: Boolean = false,
    val answerIsCorrect: Boolean? = null,
    val intervalsInDays: SpacedRepetitionIntervalsInDays? = null,
)

data class SpacedRepetitionIntervalsInDays(
    val easy: Int,
    val good: Int,
    val hard: Int,
)