package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.local.QuizCardResult
import com.gaoyun.yanyou_kototomo.data.local.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.QuizSessionId
import com.gaoyun.yanyoukototomo.data.persistence.QuizSessionsPersisted
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

fun QuizSessionsPersisted.toLocal(
): QuizSession {
    val results = Json.decodeFromString<List<QuizCardResult>>(card_results)

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