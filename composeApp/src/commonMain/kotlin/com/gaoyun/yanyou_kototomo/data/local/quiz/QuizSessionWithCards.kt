package com.gaoyun.yanyou_kototomo.data.local.quiz

import com.gaoyun.yanyou_kototomo.data.local.Card
import kotlinx.datetime.LocalDateTime

data class QuizSessionWithCards(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val results: List<QuizCardResult>,
)

data class QuizCardResult(
    val card: Card,
    val isCorrect: Boolean,
)