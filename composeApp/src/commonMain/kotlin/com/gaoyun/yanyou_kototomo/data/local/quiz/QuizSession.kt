package com.gaoyun.yanyou_kototomo.data.local.quiz

import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.persistence.QuizCardResultPersisted
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toSimpleDataEntry
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class QuizSessionId(val identifier: String)

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
                card = cards.find { c -> c.card.id.identifier == it.cardId }?.card ?: return@mapNotNull null,
                isCorrect = it.isCorrect
            )
        }
    )

    fun withDataCards(cardsWithDecks: List<Pair<CardDTO, GetDeckNames?>>): QuizSessionForStatistic {
        val resultCardIds = results.map { it.cardId }
        val cards = cardsWithDecks.map { it.first }
        val cardsFromThisQuiz = cardsWithDecks.filter { resultCardIds.contains(it.first.id) }

        return QuizSessionForStatistic(
            sessionId = sessionId,
            startTime = startTime,
            endTime = endTime,
            deckNames = cardsFromThisQuiz.mapNotNull { it.second?.name }.toSet(),
            results = results.mapNotNull {
                QuizSessionForStatisticResult(
                    card = cards.find { c -> c.id == it.cardId }?.toSimpleDataEntry() ?: return@mapNotNull null,
                    isCorrect = it.isCorrect
                )
            }
        )
    }
}