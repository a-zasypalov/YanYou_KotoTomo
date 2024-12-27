package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toSimpleDataEntry
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

    fun withDataCards(cards: List<CardDTO>) = QuizSessionWithSimpleDataEntryCards(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        results = results.mapNotNull {
            QuizCardSimpleDataEntryResult(
                card = cards.find { c -> c.id == it.card }?.toSimpleDataEntry() ?: return@mapNotNull null,
                isCorrect = it.isCorrect
            )
        }
    )
}

data class QuizSessionWithSimpleDataEntryCards(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val results: List<QuizCardSimpleDataEntryResult>,
)

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

data class QuizCardSimpleDataEntryResult(
    val card: CardSimpleDataEntry,
    val isCorrect: Boolean,
)

data class QuizSessionId(val identifier: String)