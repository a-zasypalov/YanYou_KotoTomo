package com.gaoyun.yanyou_kototomo.data.local

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

data class QuizSession(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val results: List<QuizCardResultPersisted>,
) {
    fun withCards(cards: List<CardWithProgress<*>>) = QuizSessionWithCards(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        results = results.mapNotNull {
            QuizCardResult(
                card = cards.find { c -> c.card.id.identifier == it.card }?.card ?: return@mapNotNull null,
                isCorrect = it.isCorrect
            )
        }
    )
}

data class QuizSessionWithCards(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val results: List<QuizCardResult>,
)

@Serializable
data class QuizCardResultPersisted(
    val card: String,
    val isCorrect: Boolean,
)

data class QuizCardResult(
    val card: Card,
    val isCorrect: Boolean,
)

data class QuizSessionId(val identifier: String)