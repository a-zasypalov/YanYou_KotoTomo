package domain

import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.card.CardProgress
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CardProgressUpdaterTest {

    private val mockRepository: CardsAndProgressRepository = mockk(relaxed = true)
    private lateinit var progressUpdater: CardProgressUpdater

    @BeforeEach
    fun setUp() {
        progressUpdater = CardProgressUpdater(mockRepository)
    }

    @Test
    fun `updateCardProgress should call repository with correct parameters`() {
        val cardProgress = mockk<CardProgress>()
        val deckId = mockk<DeckId>()

        progressUpdater.updateCardProgress(cardProgress, deckId)

        verify { mockRepository.updateProgress(cardProgress, deckId) }
    }

    @Test
    fun `updateCardCompletion should call repository with correct parameters`() {
        val cardId = mockk<CardId>()
        val deckId = mockk<DeckId>()
        val completed = true

        progressUpdater.updateCardCompletion(cardId, deckId, completed)

        verify { mockRepository.updateCardCompletion(cardId, deckId, completed) }
    }

    @Test
    fun `resetDeck should call repository with correct deckId`() {
        val deckId = mockk<DeckId>()

        progressUpdater.resetDeck(deckId)

        verify { mockRepository.resetDeck(deckId) }
    }
}