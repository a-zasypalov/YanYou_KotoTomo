package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.QuizCardResult
import com.gaoyun.yanyou_kototomo.data.local.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.QuizSessionId
import com.gaoyun.yanyou_kototomo.repository.QuizSessionRepository
import com.gaoyun.yanyou_kototomo.util.localDateTimeNow
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class QuizInteractor(private val repository: QuizSessionRepository) {

    fun getQuizSession(sessionId: QuizSessionId): QuizSession? = repository.getQuizSession(sessionId)

    @OptIn(ExperimentalUuidApi::class)
    fun addSession(startTime: LocalDateTime, cardResults: List<QuizCardResult>): QuizSessionId {
        val sessionId = QuizSessionId(Uuid.random().toString())
        val session = QuizSession(
            sessionId = sessionId,
            startTime = startTime,
            endTime = localDateTimeNow(),
            results = cardResults
        )
        repository.addSession(session)
        return sessionId
    }
}