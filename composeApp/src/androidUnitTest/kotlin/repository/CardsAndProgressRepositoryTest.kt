package repository

import alphabetTestCardId
import cardIdentifiers
import cardIds
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toCardsDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.util.localDateNow
import com.gaoyun.yanyoukototomo.data.persistence.Card_progressQueries
import com.gaoyun.yanyoukototomo.data.persistence.CoursesQueries
import com.gaoyun.yanyoukototomo.data.persistence.DecksQueries
import deckId
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import mockCards
import mockDecks
import mockProgressList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress as LocalCardProgress

class CardsAndProgressRepositoryTest {

    // Mock dependencies
    private val mockDatabase = mockk<YanYouKotoTomoDatabase>(relaxed = true)
    private val mockCardProgressQueries = mockk<Card_progressQueries>(relaxed = true)
    private val mockDecksQueries = mockk<DecksQueries>(relaxed = true)
    private val mockCoursesQueries = mockk<CoursesQueries>(relaxed = true)

    // Class under test
    private lateinit var repository: CardsAndProgressRepository

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { mockDatabase.card_progressQueries } returns mockCardProgressQueries
        every { mockDatabase.decksQueries } returns mockDecksQueries
        every { mockDatabase.coursesQueries } returns mockCoursesQueries
        repository = CardsAndProgressRepository(mockDatabase)
    }

    @Test
    fun `getCardProgressFor should return mapped card progress`() = runTest {
        // Arrange
        every { mockCardProgressQueries.getDeckProgress(deckId.identifier).executeAsList() } returns mockProgressList

        // Act
        val result = repository.getCardProgressFor(deckId)

        // Assert
        result shouldBe mockProgressList.map { it.toLocal() }
    }

    @Test
    fun `getCardProgressPage should return paginated card progress`() = runTest {
        // Arrange
        val page = 1
        every { mockCardProgressQueries.getAllCardsProgress(50L, 50L * page).executeAsList() } returns mockProgressList

        // Act
        val result = repository.getCardProgressPage(page)

        // Assert
        result shouldBe mockProgressList.map { it.toLocal() }
    }

    @Test
    fun `updateProgress should call updateCardProgress with correct parameters`() = runTest {
        // Arrange
        val progress = mockk<LocalCardProgress> {
            every { lastReviewed } returns localDateNow()
            every { interval } returns 1
            every { easeFactor } returns 2f
            every { nextReview } returns localDateNow()
            every { cardId } returns alphabetTestCardId.identifier
            every { completed } returns false
        }
        every { mockCardProgressQueries.updateCardProgress(any(), any(), any(), any(), any(), any(), any()) } just Runs

        // Act
        repository.updateProgress(progress, deckId)

        // Assert
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

    @Test
    fun `getCards should return mapped cards with deck names`() = runTest {
        // Arrange
        every { mockDecksQueries.getCardsByIds(cardIdentifiers).executeAsList() } returns mockCards
        every { mockCoursesQueries.getDeckNames(any()).executeAsList() } returns mockDecks

        // Act
        val result = repository.getCards(cardIdentifiers)

        // Assert
        result shouldBe mockCards.map { it.toCardsDTO() to mockDecks.find { d -> d.id == it.deck_id } }
    }

    @Test
    fun `updateCardCompletion should call updateCardProgress with correct parameters`() = runTest {
        // Arrange
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

        // Act
        repository.updateCardCompletion(alphabetTestCardId, deckId, completed)

        // Assert
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

    @Test
    fun `resetDeck should call removeProgressForDeck with correct parameters`() = runTest {
        // Arrange
        every { mockCardProgressQueries.removeProgressForDeck(deckId.identifier) } just Runs

        // Act
        repository.resetDeck(deckId)

        // Assert
        verify { mockCardProgressQueries.removeProgressForDeck(deckId.identifier) }
    }

    @Test
    fun `getFullCardsFromCache should return cards from cache`() = runTest {
        // Arrange
        every { mockDecksQueries.getCardsByIds(cardIds.map { it.identifier }).executeAsList() } returns mockCards

        // Act
        val result = repository.getFullCardsFromCache(cardIds)

        // Assert
        result shouldBe mockCards
    }
}