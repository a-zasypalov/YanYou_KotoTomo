package domain

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.repository.DeckSettingsRepository
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeckSettingsInteractorTest {

    private val mockRepository: DeckSettingsRepository = mockk()
    private lateinit var interactor: DeckSettingsInteractor

    @BeforeEach
    fun setUp() {
        interactor = DeckSettingsInteractor(mockRepository)
    }

    @Test
    fun `getDeckSettings should return settings from repository`() {
        val deckId = mockk<DeckId>()
        val expectedSettings = mockk<DeckSettings>()

        every { mockRepository.getDeckSettings(deckId) } returns expectedSettings

        val result = interactor.getDeckSettings(deckId)

        result shouldBe expectedSettings
        verify { mockRepository.getDeckSettings(deckId) }
    }

    @Test
    fun `updateDeckSettings should call repository with given settings`() {
        val settings = mockk<DeckSettings>()

        every { mockRepository.saveDeckSettings(settings) } just Runs

        interactor.updateDeckSettings(settings)

        verify { mockRepository.saveDeckSettings(settings) }
    }
}