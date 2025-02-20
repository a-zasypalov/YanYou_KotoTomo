package domain

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import courseDeck
import courseDeckDto
import courseDto
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import rootStructureDTO

class GetCoursesRootTest {

    private val repository: CoursesRootComponentRepository = mockk()
    private val preferences: Preferences = mockk()
    private val bookmarksInteractor: BookmarksInteractor = mockk()

    private val getCoursesRoot = GetCoursesRoot(
        repository = repository,
        preferences = preferences,
        bookmarksInteractor = bookmarksInteractor
    )


    @Test
    fun `getCourses should return sorted RootStructure and update bookmarks`() = runBlocking {
        val rootStructure = rootStructureDTO.toLocal()

        val expectedSortedRootStructure = rootStructure.copy(languages = rootStructure.languages.sortedBy {
            if (it.id.identifier == "cn") 0 else 1
        })

        every { preferences.getString(PreferencesKeys.PRIMARY_LANGUAGE_ID, "cn") } returns "cn"
        coEvery { repository.getCoursesRoot(false) } returns rootStructureDTO
        every { bookmarksInteractor.getBookmarkedDecks() } returns listOf(courseDeck)
        every { bookmarksInteractor.getLearningDeck() } returns courseDeck
        every { bookmarksInteractor.saveBookmarkedDecks(any()) } just Runs
        coEvery { bookmarksInteractor.setLearningDeckId(any()) } just Runs

        val result = getCoursesRoot.getCourses()

        result shouldBe expectedSortedRootStructure

        verify { bookmarksInteractor.saveBookmarkedDecks(listOf(courseDeckDto.toLocal())) }
    }

    @Test
    fun `getCourseLanguages should return list of LanguageId`() = runTest {
        coEvery { repository.getCoursesRoot(false) } returns rootStructureDTO

        val result = getCoursesRoot.getCourseLanguages()

        result shouldBe listOf(LanguageId("cn"))
    }

    @Test
    fun `getCourseDecks should return the correct course decks`() = runTest {
        val courseId = CourseId("course1")
        coEvery { repository.getCourse(courseId) } returns courseDto
        val result = getCoursesRoot.getCourseDecks(courseId)
        result shouldBe courseDto.toLocal()
    }
}