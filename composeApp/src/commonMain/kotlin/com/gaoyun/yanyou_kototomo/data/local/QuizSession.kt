package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toSimpleDataEntry
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames
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
                card = cards.find { c -> c.card.id.identifier == it.cardId }?.card ?: return@mapNotNull null,
                isCorrect = it.isCorrect
            )
        }
    )

    fun withDataCards(cardsWithDecks: List<Pair<CardDTO, GetDeckNames?>>) = QuizSessionWithSimpleDataEntryCards(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        deckNames = cardsWithDecks.filter { results.map { it.cardId }.contains(it.first.id) }.mapNotNull { it.second?.name }.toSet(),
        results = results.mapNotNull {
            QuizCardSimpleDataEntryResult(
                card = cardsWithDecks.find { c -> c.first.id == it.cardId }?.first?.toSimpleDataEntry() ?: return@mapNotNull null,
                isCorrect = it.isCorrect
            )
        }
    )
}

data class QuizSessionWithSimpleDataEntryCards(
    val sessionId: QuizSessionId,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val deckNames: Set<String>,
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
    val cardId: String,
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