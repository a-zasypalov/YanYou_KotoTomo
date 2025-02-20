package domain

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.domain.SpacialRepetitionSettingsInteractor
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class SpacialRepetitionSettingsInteractorTest {

    private val preferences: Preferences = mockk(relaxed = true)
    private val interactor = SpacialRepetitionSettingsInteractor(preferences)

    @Test
    fun `easeFactor should fetch value from preferences`() {
        every { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_EASE_FACTOR, 2.0f) } returns 2.5f
        interactor.easeFactor() shouldBe 2.5f
        verify { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_EASE_FACTOR, 2.0f) }
    }

    @Test
    fun `setEaseFactor should store value in preferences`() {
        interactor.setEaseFactor(2.5f)
        verify { preferences.setFloat(PreferencesKeys.SPACIAL_REPETITION_EASE_FACTOR, 2.5f) }
    }

    @Test
    fun `intervalBase should fetch value from preferences`() {
        every { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_INTERVAL_BASE, 1.5f) } returns 1.6f
        interactor.intervalBase() shouldBe 1.6f
        verify { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_INTERVAL_BASE, 1.5f) }
    }

    @Test
    fun `setIntervalBase should store value in preferences`() {
        interactor.setIntervalBase(1.6f)
        verify { preferences.setFloat(PreferencesKeys.SPACIAL_REPETITION_INTERVAL_BASE, 1.6f) }
    }

    @Test
    fun `easyAnswerWeight should fetch value from preferences`() {
        every { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_EASY_ANSWER_WEIGHT, 0.2f) } returns 0.25f
        interactor.easyAnswerWeight() shouldBe 0.25f
        verify { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_EASY_ANSWER_WEIGHT, 0.2f) }
    }

    @Test
    fun `setEasyAnswerWeight should store value in preferences`() {
        interactor.setEasyAnswerWeight(0.25f)
        verify { preferences.setFloat(PreferencesKeys.SPACIAL_REPETITION_EASY_ANSWER_WEIGHT, 0.25f) }
    }

    @Test
    fun `goodAnswerWeight should fetch value from preferences`() {
        every { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT, 0.1f) } returns 0.15f
        interactor.goodAnswerWeight() shouldBe 0.15f
        verify { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT, 0.1f) }
    }

    @Test
    fun `setGoodAnswerWeight should store value in preferences`() {
        interactor.setGoodAnswerWeight(0.15f)
        verify { preferences.setFloat(PreferencesKeys.SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT, 0.15f) }
    }

    @Test
    fun `hardAnswerWeight should fetch value from preferences`() {
        every { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_HARD_ANSWER_WEIGHT, 0.2f) } returns 0.3f
        interactor.hardAnswerWeight() shouldBe 0.3f
        verify { preferences.getFloat(PreferencesKeys.SPACIAL_REPETITION_HARD_ANSWER_WEIGHT, 0.2f) }
    }

    @Test
    fun `setHardAnswerWeight should store value in preferences`() {
        interactor.setHardAnswerWeight(0.3f)
        verify { preferences.setFloat(PreferencesKeys.SPACIAL_REPETITION_HARD_ANSWER_WEIGHT, 0.3f) }
    }
}