package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionForStatistic
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionWithCards
import com.gaoyun.yanyou_kototomo.data.persistence.QuizCardResultPersisted
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.repository.QuizSessionRepository
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import com.gaoyun.yanyou_kototomo.util.localDateTimeNow
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class QuizInteractor(
    private val repository: QuizSessionRepository,
    private val getDeck: GetDeck,
    private val getCoursesRoot: GetCoursesRoot,
    private val cardsRepository: CardsAndProgressRepository,
) {

    fun getSessionsPage(page: Int): List<QuizSessionForStatistic> {
        val sessions = repository.getQuizSessions(page)
        val sessionsCardsIds = sessions.flatMap { it.results.map { it.cardId } }
        val cardsAndDeckNames = cardsRepository.getCards(sessionsCardsIds)
        return sessions.map { it.withDataCards(cardsAndDeckNames) }
    }

    fun deleteSession(id: QuizSessionId) {
        repository.deleteQuizSession(id)
    }

    suspend fun getQuizSession(
        args: QuizSessionSummaryArgs,
    ): QuizSessionWithCards? {
        val session = repository.getQuizSession(args.sessionId) ?: return null
        val cardIds = session.results.map { it.cardId }
        val course = getCoursesRoot.getCourseDecks(args.courseId)
        val cards = course.decks.find { it.id == args.deckId }?.let { deckInCourse ->
            val deck = getDeck.getDeck(
                learningLanguage = args.learningLanguageId,
                sourceLanguage = args.sourceLanguageId,
                deck = deckInCourse,
                requiredDecks = course.requiredDecks ?: listOf()
            )
            deck?.cards
        }?.filter { cardIds.contains(it.card.id.identifier) } ?: listOf()
        return session.withCards(cards)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addSession(startTime: LocalDateTime, cardResults: List<QuizCardResultPersisted>): QuizSessionId {
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