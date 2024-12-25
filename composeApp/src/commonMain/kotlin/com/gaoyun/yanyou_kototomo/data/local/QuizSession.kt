package com.gaoyun.yanyou_kototomo.data.local

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

data class QuizSession(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val results: List<QuizCardResult>,
)

@Serializable
data class QuizCardResult(
    val cardId: CardId,
    val isCorrect: Boolean,
)

data class QuizSessionId(val identifier: String)