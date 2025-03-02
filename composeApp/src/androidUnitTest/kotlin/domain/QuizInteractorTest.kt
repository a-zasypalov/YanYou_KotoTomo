package domain

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSession
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.data.persistence.QuizCardResultPersisted
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.repository.QuizSessionRepository
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerBackRoute
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames
import courseDto
import courseId
import deckDTO
import deckId
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuizInteractorTest {

    private val mockRepository = mockk<QuizSessionRepository>(relaxed = true)
    private val mockGetDeck = mockk<GetDeck>(relaxed = true)
    private val mockGetCoursesRoot = mockk<GetCoursesRoot>(relaxed = true)
    private val mockCardsRepository = mockk<CardsAndProgressRepository>(relaxed = true)

    private val quizInteractor = QuizInteractor(
        repository = mockRepository,
        getDeck = mockGetDeck,
        getCoursesRoot = mockGetCoursesRoot,
        cardsRepository = mockCardsRepository
    )

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `getSessionsPage should return sessions with card data`() {
        // Arrange
        val page = 1
        val session1 = QuizSession(
            sessionId = QuizSessionId("1"),
            startTime = LocalDateTime(2023, 10, 2, 12, 0),
            endTime = LocalDateTime(2023, 10, 2, 12, 30),
            results = listOf(QuizCardResultPersisted(cardId = "card1", isCorrect = true))
        )
        val session2 = QuizSession(
            sessionId = QuizSessionId("2"),
            startTime = LocalDateTime(2023, 10, 2, 12, 0),
            endTime = LocalDateTime(2023, 10, 2, 12, 30),
            results = listOf(QuizCardResultPersisted(cardId = "card2", isCorrect = true))
        )

        val sessions = listOf(session1, session2)
        val cardIds = listOf("card1", "card2")
        val cardsAndDeckNames = listOf(
            CardDTO.WordCardDTO(
                id = "card1",
                character = "",
                transcription = "",
                translation = "",
                additionalInfo = "",
                speechPart = "noun"
            ) to GetDeckNames(id = "deck1", name = "deck1"),
            CardDTO.WordCardDTO(
                id = "card2",
                character = "",
                transcription = "",
                translation = "",
                additionalInfo = "",
                speechPart = "noun"
            ) to GetDeckNames(id = "deck2", name = "deck2")
        )

        every { mockRepository.getQuizSessions(page) } returns sessions
        every { mockCardsRepository.getCards(cardIds) } returns cardsAndDeckNames

        // Act
        val result = quizInteractor.getSessionsPage(page)

        // Assert
        result.size shouldBe 2
        verify { mockRepository.getQuizSessions(page) }
        verify { mockCardsRepository.getCards(cardIds) }
    }

    @Test
    fun `deleteSession should call repository delete method`() {
        // Arrange
        val sessionId = QuizSessionId("1")
        every { mockRepository.deleteQuizSession(sessionId) } returns Unit

        // Act
        quizInteractor.deleteSession(sessionId)

        // Assert
        verify { mockRepository.deleteQuizSession(sessionId) }
    }

    @Test
    fun `getQuizSession should return session with cards`() = runTest {
        // Arrange
        val sessionId = QuizSessionId("1")
        val learningLanguageId = LanguageId("cn")
        val sourceLanguageId = LanguageId("en")
        val args = QuizSessionSummaryArgs(
            sessionId = sessionId,
            courseId = courseId,
            deckId = deckId,
            learningLanguageId = learningLanguageId,
            sourceLanguageId = sourceLanguageId,
            backToRoute = PlayerBackRoute.Home,
        )
        val session = QuizSession(
            sessionId = sessionId,
            startTime = LocalDateTime(2023, 10, 1, 12, 0),
            endTime = LocalDateTime(2023, 10, 1, 12, 30),
            results = listOf(QuizCardResultPersisted(cardId = "card1", isCorrect = true))
        )
        val course = courseDto.toLocal()
        val deck = deckDTO.toLocal("", listOf())

        every { mockRepository.getQuizSession(sessionId) } returns session
        coEvery { mockGetCoursesRoot.getCourse(courseId) } returns course
        coEvery {
            mockGetDeck.getDeck(
                learningLanguageId,
                sourceLanguageId,
                course.decks[0],
                course.requiredDecks ?: listOf()
            )
        } returns deck

        // Act
        val result = quizInteractor.getQuizSession(args)

        // Assert
        result?.sessionId shouldBe sessionId
        verify { mockRepository.getQuizSession(sessionId) }
        coVerify { mockGetCoursesRoot.getCourse(courseId) }
        coVerify { mockGetDeck.getDeck(learningLanguageId, sourceLanguageId, course.decks[0], course.requiredDecks ?: listOf()) }
    }

    @Test
    fun `addSession should add session to repository and return session ID`() {
        // Arrange
        val startTime = LocalDateTime(2023, 10, 1, 12, 0)
        val cardResults = listOf(QuizCardResultPersisted(cardId = "card1", isCorrect = true))

        // Act
        quizInteractor.addSession(startTime, cardResults)

        // Assert
        verify { mockRepository.addSession(any()) }
    }
}