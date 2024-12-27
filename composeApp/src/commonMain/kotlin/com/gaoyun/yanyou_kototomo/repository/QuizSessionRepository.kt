package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.QuizCardResultPersisted
import com.gaoyun.yanyou_kototomo.data.local.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class QuizSessionRepository(private val db: YanYouKotoTomoDatabase) {
    companion object {
        const val PAGE_SIZE = 50L
    }

    fun getQuizSession(sessionId: QuizSessionId): QuizSession? {
        return db.quiz_sessionQueries.getQuizSession(sessionId.identifier).executeAsOneOrNull()?.let { session ->
            val cards = db.decksQueries.getCardsByIds(session.card_ids).executeAsList()
            session.toLocal(cards)
        }
    }

    fun getQuizSessions(page: Int): List<QuizSession> {
        return db.quiz_sessionQueries.getQuizSessions(PAGE_SIZE, PAGE_SIZE * page).executeAsList().map { session ->
            val cards = db.decksQueries.getCardsByIds(session.card_ids).executeAsList()
            session.toLocal(cards)
        }
    }

    fun addSession(session: QuizSession) {
        db.quiz_sessionQueries.insertQuizSession(
            sessionId = session.sessionId.identifier,
            startTime = session.startTime.toString(),
            endTime = session.endTime.toString(),
            cardIds = session.results.map { it.card },
            cardResults = Json.encodeToString(ListSerializer(QuizCardResultPersisted.serializer()), session.results)
        )
    }
}