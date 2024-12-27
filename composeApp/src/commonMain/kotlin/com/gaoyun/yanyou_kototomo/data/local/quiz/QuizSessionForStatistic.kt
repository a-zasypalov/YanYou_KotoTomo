package com.gaoyun.yanyou_kototomo.data.local.quiz

import com.gaoyun.yanyou_kototomo.data.local.CardSimpleDataEntry
import kotlinx.datetime.LocalDateTime

data class QuizSessionForStatistic(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val deckNames: Set<String>,
    val results: List<QuizSessionForStatisticResult>,
)

data class QuizSessionForStatisticResult(
    val card: CardSimpleDataEntry,
    val isCorrect: Boolean,
)