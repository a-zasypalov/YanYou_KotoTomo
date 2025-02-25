package domain

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.domain.AllDataReset
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AllDataResetTest {

    // Mock dependencies
    private val mockPreferences: Preferences = mockk(relaxed = true)
    private val mockDb: YanYouKotoTomoDatabase = mockk(relaxed = true)

    // Class under test
    private lateinit var allDataReset: AllDataReset

    // Reset mocks before each test
    @BeforeEach
    fun setUp() {
        clearAllMocks()
        allDataReset = AllDataReset(mockPreferences, mockDb)
    }

    @Test
    fun `resetApp should call clearDatabase and clearPreferences`() = runTest {
        // Act
        allDataReset.resetApp()

        // Assert
        verify(exactly = 1) { allDataReset.clearDatabase() }
        verify(exactly = 1) { allDataReset.clearPreferences() }
    }

    @Test
    fun `clearDatabase should delete all data from database tables`() = runTest {
        // Act
        allDataReset.clearDatabase()

        // Assert
        verify(exactly = 1) { mockDb.card_progressQueries.deleteAll() }
        verify(exactly = 1) { mockDb.coursesQueries.clearCache() }
        verify(exactly = 1) { mockDb.deck_settingsQueries.deleteAll() }
        verify(exactly = 1) { mockDb.decksQueries.deleteAll() }
        verify(exactly = 1) { mockDb.quiz_sessionQueries.deleteAll() }
        verify(exactly = 1) { mockDb.updatesQueries.deleteAll() }
    }

    @Test
    fun `clearPreferences should remove all specified preferences`() = runTest {
        // Act
        allDataReset.clearPreferences()

        // Assert
        verify(exactly = 1) { mockPreferences.remove(PreferencesKeys.UPDATES_STRUCTURE_REFRESHED) }
        verify(exactly = 1) { mockPreferences.remove(PreferencesKeys.UPDATES_COURSES_REFRESHED) }
        verify(exactly = 1) { mockPreferences.remove(PreferencesKeys.LEARNING_LANGUAGE) }
        verify(exactly = 1) { mockPreferences.remove(PreferencesKeys.BOOKMARKED_DECKS) }
    }
}