package domain

import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import courseDeck
import courseDeckDto
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rootStructureDTO

class BookmarksInteractorTest {

    private val mockPreferences: Preferences = mockk(relaxed = true)
    private val mockCoursesRootComponentRepository: CoursesRootComponentRepository = mockk(relaxed = true)

    private lateinit var interactor: BookmarksInteractor

    @BeforeEach
    fun setUp() {
        interactor = BookmarksInteractor(preferences = mockPreferences, coursesRootComponentRepository = mockCoursesRootComponentRepository)
        clearAllMocks()
    }

    @Test
    fun `getLearningDeck returns emoty list if LEARNING_DECKS is not in preferences`() = runTest {
        every { mockPreferences.getString(PreferencesKeys.LEARNING_LANGUAGE) } returns null

        interactor.getLearningCourse() shouldBe null
    }

    @Test
    fun `getBookmarkedDecks returns list of CourseDecks`() {
        val decksDto = listOf(courseDeckDto)
        val mockJson = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), decksDto)

        val expectedDecks = listOf(courseDeck)

        every { mockPreferences.getString(PreferencesKeys.BOOKMARKED_DECKS, "[]") } returns mockJson

        interactor.getBookmarkedDecks() shouldBe expectedDecks
    }

    @Test
    fun `getBookmarkedDecks returns empty list if deserialization fails`() {
        val mockJson = ""

        every { mockPreferences.getString(PreferencesKeys.BOOKMARKED_DECKS, "[]") } returns mockJson
        mockkObject(Json)
        every { Json.decodeFromString(ListSerializer(CourseDeckDTO.serializer()), mockJson) } throws SerializationException("")

        interactor.getBookmarkedDecks() shouldBe emptyList()
    }

    @Test
    fun `addDeck should correctly save bookmarked decks`() = runTest {
        val mockDeckId = courseDeck.id
        val mockCourseDecks = listOf(courseDeck)
        val deckToAdd = courseDeck
        val decks = listOf<CourseDeck>()

        interactor.courseDecks.value = mockCourseDecks

        interactor.addBookmark(mockDeckId, decks)

        val expectedDecksToSave = decks + deckToAdd
        verify { interactor.saveBookmarkedDecks(expectedDecksToSave) }
    }

    @Test
    fun `saveBookmarkedDecks should serialize and save decks`() {
        val decks = listOf(courseDeck)
        val decksDto = listOf(courseDeckDto)

        val json = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), decksDto)

        interactor.saveBookmarkedDecks(decks)

        verify { mockPreferences.setString(PreferencesKeys.BOOKMARKED_DECKS, json) }
    }

    @Test
    fun `getCourseDecks returns correct course decks`() = runTest {
        coEvery { mockCoursesRootComponentRepository.getCoursesRoot(any()) } returns rootStructureDTO

        interactor.getCourseDecks() shouldBe listOf(courseDeck)
    }
}