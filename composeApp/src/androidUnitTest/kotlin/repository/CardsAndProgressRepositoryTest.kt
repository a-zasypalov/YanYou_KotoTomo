package repository

import alphabetTestCardId
import cardIdentifiers
import cardIds
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toCardsDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.util.localDateNow
import deckId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import mockCardProgressQueries
import mockCards
import mockCoursesQueries
import mockDatabase
import mockDecks
import mockDecksQueries
import mockProgressList
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress as LocalCardProgress

class CardsAndProgressRepositoryTest : StringSpec({
    val repository = CardsAndProgressRepository(mockDatabase)

    beforeTest {
        every { mockDatabase.card_progressQueries } returns mockCardProgressQueries
        every { mockDatabase.decksQueries } returns mockDecksQueries
        every { mockDatabase.coursesQueries } returns mockCoursesQueries
    }

    "getCardProgressFor should return mapped card progress" {
        every { mockCardProgressQueries.getDeckProgress(deckId.identifier).executeAsList() } returns mockProgressList
        val result = repository.getCardProgressFor(deckId)
        result shouldBe mockProgressList.map { it.toLocal() }
    }

    "getCardProgressPage should return paginated card progress" {
        val page = 1
        every { mockCardProgressQueries.getAllCardsProgress(50L, 50L * page).executeAsList() } returns mockProgressList
        val result = repository.getCardProgressPage(page)
        result shouldBe mockProgressList.map { it.toLocal() }
    }

    "updateProgress should call updateCardProgress with correct parameters" {
        val progress = mockk<LocalCardProgress> {
            every { lastReviewed } returns localDateNow()
            every { interval } returns 1
            every { easeFactor } returns 2f
            every { nextReview } returns localDateNow()
            every { cardId } returns alphabetTestCardId.identifier
            every { completed } returns false
        }
        every { mockCardProgressQueries.updateCardProgress(any(), any(), any(), any(), any(), any(), any()) } just Runs

        repository.updateProgress(progress, deckId)

        verify {
            mockCardProgressQueries.updateCardProgress(
                lastReviewed = localDateNow().toString(),
                interval = 1,
                easeFactor = 2.0,
                nextReview = localDateNow().toString(),
                card_id = alphabetTestCardId.identifier,
                deck_id = deckId.identifier,
                completed = false
            )
        }
    }

    "getCards should return mapped cards with deck names" {
        every { mockDecksQueries.getCardsByIds(cardIdentifiers).executeAsList() } returns mockCards
        every { mockCoursesQueries.getDeckNames(any()).executeAsList() } returns mockDecks
        val result = repository.getCards(cardIdentifiers)
        result shouldBe mockCards.map { it.toCardsDTO() to mockDecks.find { d -> d.id == it.deck_id } }
    }

    "updateCardCompletion should call updateCardProgress with correct parameters" {
        val completed = true

        every {
            mockCardProgressQueries.updateCardProgress(
                lastReviewed = null,
                interval = null,
                easeFactor = null,
                nextReview = null,
                card_id = alphabetTestCardId.identifier,
                deck_id = deckId.identifier,
                completed = true
            )
        } just Runs

        repository.updateCardCompletion(alphabetTestCardId, deckId, completed)

        verify {
            mockCardProgressQueries.updateCardProgress(
                lastReviewed = null,
                interval = null,
                easeFactor = null,
                nextReview = null,
                card_id = alphabetTestCardId.identifier,
                deck_id = deckId.identifier,
                completed = true
            )
        }
    }

    "resetDeck should call removeProgressForDeck with correct parameters" {
        every { mockCardProgressQueries.removeProgressForDeck(deckId.identifier) } just Runs
        repository.resetDeck(deckId)
        verify { mockCardProgressQueries.removeProgressForDeck(deckId.identifier) }
    }

    "getFullCardsFromCache should return cards from cache" {
        every { mockDecksQueries.getCardsByIds(cardIds.map { it.identifier }).executeAsList() } returns mockCards
        val result = repository.getFullCardsFromCache(cardIds)
        result shouldContainExactly mockCards
    }
})
