package repository

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.mapToRootStructureDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import com.gaoyun.yanyou_kototomo.repository.DeckUpdatesRepository
import com.gaoyun.yanyoukototomo.data.persistence.CoursesQueries
import courseId
import io.kotest.core.spec.style.AnnotationSpec.BeforeEach
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runTest
import mockGetCourse
import org.junit.jupiter.api.Test
import rootStructureDTO

class CoursesRootComponentRepositoryTest {

    private val mockApi: DecksApi = mockk(relaxed = true)
    private val mockDb: YanYouKotoTomoDatabase = mockk(relaxed = true)
    private val mockPrefs: Preferences = mockk(relaxed = true)
    private val mockDeckRepository: DeckRepository = mockk(relaxed = true)
    private val mockDeckUpdatesRepository: DeckUpdatesRepository = mockk(relaxed = true)
    private val mockCoursesQueries: CoursesQueries = mockk(relaxed = true)

    private val repository = CoursesRootComponentRepository(
        api = mockApi,
        db = mockDb,
        prefs = mockPrefs,
        deckRepository = mockDeckRepository,
        deckUpdatesRepository = mockDeckUpdatesRepository
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `getCoursesRoot fetches api if no cache exists`() = runTest {
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } returns false
        every { mockCoursesQueries.getRootData().executeAsList().mapToRootStructureDTO() } returns null
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO

        val result = repository.getCoursesRoot(force = false)

        result shouldBe rootStructureDTO
        coVerify { mockApi.getCoursesRootComponent() }
    }

    @Test
    fun `getCoursesRoot fetches api if cache should be refreshed`() = runTest {
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } returns true
        every { mockCoursesQueries.getRootData().executeAsList().mapToRootStructureDTO() } returns rootStructureDTO
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO

        val result = repository.getCoursesRoot(force = false)

        result shouldBe rootStructureDTO
        coVerify { mockApi.getCoursesRootComponent() }
    }

    @Test
    fun `getCoursesRoot fetches api if force reload is true`() = runTest {
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } returns false
        every { mockCoursesQueries.getRootData().executeAsList().mapToRootStructureDTO() } returns rootStructureDTO
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO

        val result = repository.getCoursesRoot(force = true)

        result shouldBe rootStructureDTO
        coVerify { mockApi.getCoursesRootComponent() }
    }

    @Test
    fun `getCourse fetches api if shouldRefreshCourses`() = runTest {
        var callCount = 0
        coEvery { mockDeckUpdatesRepository.shouldRefreshCourses() } answers {
            callCount++
            when (callCount) {
                1 -> true
                else -> false
            }
        }
        coEvery { mockApi.getCoursesRootComponent() } returns rootStructureDTO
        every { mockDb.coursesQueries.getCourse(courseId.identifier).executeAsList() } returns listOf(mockGetCourse)

        val result = repository.getCourse(courseId)

        result shouldBe listOf(mockGetCourse).toDTO()
        coVerify { mockApi.getCoursesRootComponent() }
    }
}