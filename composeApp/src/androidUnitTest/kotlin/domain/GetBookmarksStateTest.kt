package domain

import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.GetUserSavedDecks
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeckFromCache
import courseDeck
import deckWithCourseInfo
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rootStructureDTO

class GetBookmarksStateTest {

    private val bookmarksInteractor: BookmarksInteractor = mockk()
    private val getDeckFromCache: GetDeckFromCache = mockk()
    private val getCoursesRoot: GetCoursesRoot = mockk()
    private val deckSettingsInteractor: DeckSettingsInteractor = mockk()

    private lateinit var courseDeckToDeckWithCourseInfo: GetUserSavedDecks

    @BeforeEach
    fun setUp() {
        courseDeckToDeckWithCourseInfo = GetUserSavedDecks(
            bookmarksInteractor = bookmarksInteractor,
            getDeckFromCache = getDeckFromCache,
            getCoursesRoot = getCoursesRoot,
            deckSettingsInteractor = deckSettingsInteractor
        )
    }

    @Test
    fun `getBookmarks should return list of DeckWithCourseInfo`() = runBlocking {
        val sampleRootStructure = rootStructureDTO.toLocal()
        val sampleBookmarkedDeck = courseDeck
        val deckWithInfo = deckWithCourseInfo

        // Mock responses
        coEvery { getCoursesRoot.getCourses() } returns sampleRootStructure
        every { bookmarksInteractor.getBookmarkedDecks() } returns listOf(sampleBookmarkedDeck)
        every { deckSettingsInteractor.getDeckSettings(any()) } returns mockk {
            every { pausedCards } returns setOf()
        }

        val expectedDecksWithCourseInfo = listOf(deckWithInfo)

        coEvery { getDeckFromCache.getDeck(sampleBookmarkedDeck, any()) } returns mockk {
            every { withInfo(any()) } returns deckWithInfo
        }

        // Call method and assert
        val result = courseDeckToDeckWithCourseInfo.getBookmarks()

        result.shouldContainExactly(expectedDecksWithCourseInfo)

        coVerify { getCoursesRoot.getCourses() }
        verify { bookmarksInteractor.getBookmarkedDecks() }
        verify { deckSettingsInteractor.getDeckSettings(any()) }
        verify { getDeckFromCache.getDeck(sampleBookmarkedDeck, any()) }
    }
}