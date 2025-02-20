package domain

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.domain.OnboardingInteractor
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class OnboardingInteractorTest {

    private val preferences: Preferences = mockk(relaxed = true)
    private val onboardingInteractor = OnboardingInteractor(preferences)

    @Test
    fun `getOnboardingIsShown should return the boolean value from preferences`() {
        // Setting up expected interactions
        every { preferences.getBoolean(PreferencesKeys.ONBOARDING_IS_SHOWN, false) } returns true

        // Execute
        val result = onboardingInteractor.getOnboardingIsShown()

        // Verify
        result shouldBe true
        verify { preferences.getBoolean(PreferencesKeys.ONBOARDING_IS_SHOWN, false) }
    }

    @Test
    fun `setOnboardingIsShown should set the onboarding shown flag to true`() {
        // Execute
        onboardingInteractor.setOnboardingIsShown()

        // Verify that the boolean was set correctly
        verify { preferences.setBoolean(PreferencesKeys.ONBOARDING_IS_SHOWN, true) }
    }

    @Test
    fun `setPrimaryLanguage should store the language ID in preferences`() {
        val languageId = LanguageId("en")

        // Execute
        onboardingInteractor.setPrimaryLanguage(languageId)

        // Verify that the primary language ID was stored correctly
        verify { preferences.setString(PreferencesKeys.PRIMARY_LANGUAGE_ID, languageId.identifier) }
    }
}