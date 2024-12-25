package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.QuizCardResult
import com.gaoyun.yanyou_kototomo.data.local.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class QuizSessionRepository(private val db: YanYouKotoTomoDatabase) {

    fun getQuizSession(sessionId: QuizSessionId): QuizSession? {
        return db.quiz_sessionQueries.getQuizSession(sessionId.identifier).executeAsOneOrNull()?.toLocal()
    }

    fun addSession(session: QuizSession) {
        db.quiz_sessionQueries.insertQuizSession(
            sessionId = session.sessionId.identifier,
            startTime = session.startTime.toString(),
            endTime = session.endTime.toString(),
            cardIds = session.results.map { it.cardId.identifier },
            cardResults = Json.encodeToString(ListSerializer(QuizCardResult.serializer()), session.results)
        )
    }
}