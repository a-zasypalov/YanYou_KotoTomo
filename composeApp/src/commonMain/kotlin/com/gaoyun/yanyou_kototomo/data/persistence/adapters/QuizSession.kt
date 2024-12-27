package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.persistence.QuizCardResultPersisted
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted
import com.gaoyun.yanyoukototomo.data.persistence.QuizSessionsPersisted
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

fun QuizSessionsPersisted.toLocal(
    cards: List<CardsPersisted>,
): QuizSession {
    val results = Json.decodeFromString<List<QuizCardResultPersisted>>(card_results).mapNotNull { cardPersisted ->
        QuizCardResultPersisted(
            cardId = cards.find { c -> c.id == cardPersisted.cardId }?.id ?: return@mapNotNull null,
            isCorrect = cardPersisted.isCorrect
        )
    }

    // assuming ISO format (e.g., "2024-12-25T10:30:00")
    val start = LocalDateTime.parse(start_time)
    val end = LocalDateTime.parse(end_time)

    return QuizSession(
        sessionId = QuizSessionId(session_id),
        startTime = start,
        endTime = end,
        results = results
    )
}