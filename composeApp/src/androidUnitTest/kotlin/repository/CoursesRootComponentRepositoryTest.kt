package repository

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.mapToRootStructureDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.LearningLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.data.remote.SourceLanguageDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import com.gaoyun.yanyou_kototomo.repository.DeckUpdatesRepository
import com.gaoyun.yanyoukototomo.data.persistence.CoursesQueries
import com.gaoyun.yanyoukototomo.data.persistence.DecksQueries
import com.gaoyun.yanyoukototomo.data.persistence.GetCourse
import courseId
import deckId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class CoursesRootComponentRepositoryTest : StringSpec({

    val mockApi: DecksApi = mockk(relaxed = true)
    val mockDb: YanYouKotoTomoDatabase = mockk(relaxed = true)
    val mockPrefs: Preferences = mockk(relaxed = true)
    val mockDeckRepository: DeckRepository = mockk(relaxed = true)
    val mockDeckUpdatesRepository: DeckUpdatesRepository = mockk(relaxed = true)
    val mockCoursesQueries: CoursesQueries = mockk(relaxed = true)

    val mockGetCourse = GetCourse(
        course_id = "course1",
        course_name = "Course 1",
        preview = "Preview of Course 1",
        required_decks = listOf("deck1", "deck2"), // List of required deck IDs
        deck_id = "deck1", // Optional deck ID
        deck_name = "Deck 1", // Optional deck name
        deck_preview = "Preview of Deck 1", // Optional deck preview
        deck_version = 1L, // Optional deck version
        deck_type = "Alphabet", // Optional deck type
        deck_alphabet = "hiragana" // Optional deck alphabet
    )

    val courseDto = CourseDTO(
        id = courseId.identifier,
        courseName = "Course 1",
        preview = "Preview",
        requiredDecks = listOf(deckId.identifier),
        decks = listOf(
            CourseDeckDTO.Alphabet(
                id = "deck1",
                name = "Deck 1",
                preview = "Preview",
                version = 1,
                alphabet = "hiragana"
            )
        )
    )
    // Test values
    val rootStructureDTO = RootStructureDTO(
        languages = listOf(
            LearningLanguageDTO(
                id = "lang1",
                sourceLanguages = listOf(
                    SourceLanguageDTO(
                        sourceLanguage = "sourceLang1",
                        courses = listOf(courseDto)
                    )
                )
            )
        )
    )

    val repository = CoursesRootComponentRepository(
        api = mockApi,
        db = mockDb,
        prefs = mockPrefs,
        deckRepository = mockDeckRepository,
        deckUpdatesRepository = mockDeckUpdatesRepository
    )

    beforeTest {
        clearAllMocks()
    }

    "getCoursesRoot fetches api if no cache exists" {
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } returns false
        every { mockCoursesQueries.getRootData().executeAsList().mapToRootStructureDTO() } returns null
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO

        val result = repository.getCoursesRoot(force = false)

        result shouldBe rootStructureDTO
        coVerify { mockApi.getCoursesRootComponent() }
    }

    "getCoursesRoot fetches api if cache should be refreshed" {
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } returns true
        every { mockCoursesQueries.getRootData().executeAsList().mapToRootStructureDTO() } returns rootStructureDTO
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO

        val result = repository.getCoursesRoot(force = false)

        result shouldBe rootStructureDTO
        coVerify { mockApi.getCoursesRootComponent() }
    }

    "getCoursesRoot fetches api if force reload is true" {
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } returns false
        every { mockCoursesQueries.getRootData().executeAsList().mapToRootStructureDTO() } returns rootStructureDTO
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO

        val result = repository.getCoursesRoot(force = true)

        result shouldBe rootStructureDTO
        coVerify { mockApi.getCoursesRootComponent() }
    }

    "getCourse fetches api if shouldRefreshCourses" {
        var callCount = 0
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } answers {
            callCount++
            when (callCount) {
                1 -> true  // First call returns true
                else -> false // Second call returns false
            }
        }
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO
        every { mockDb.coursesQueries.getCourse(courseId.identifier).executeAsList() } returns listOf(mockGetCourse)

        val result = repository.getCourse(courseId)

        result shouldBe listOf(mockGetCourse).toDTO()
        coVerify { mockApi.getCoursesRootComponent() }
    }
})