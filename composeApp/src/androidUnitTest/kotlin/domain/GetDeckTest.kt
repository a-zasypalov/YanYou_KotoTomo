package domain

import cardProgress
import com.gaoyun.yanyou_kototomo.data.local.*
import com.gaoyun.yanyou_kototomo.data.local.card.withProgress
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import com.gaoyun.yanyou_kototomo.data.remote.converters.*
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import courseDeck
import deckDTO
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.coEvery
import kanaCardDTO
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import wordCardDTO

class GetDeckTest {

    private val deckRepository: DeckRepository = mockk()
    private val cardsAndProgressRepository: CardsAndProgressRepository = mockk()

    private val getDeck = GetDeck(
        deckRepository = deckRepository,
        cardsAndProgressRepository = cardsAndProgressRepository
    )

    val learningLanguage = LanguageId("en")
    val sourceLanguage = LanguageId("jp")
    val deckId = DeckId("deck1")

    @Test
    fun `getDeck should return a populated Deck with cards and progress`() = runBlocking {
        val courseDeck = courseDeck

        coEvery { deckRepository.getDeck(learningLanguage, sourceLanguage, courseDeck) } returns deckDTO
        every { cardsAndProgressRepository.getCardProgressFor(deckId) } returns listOf(cardProgress.toLocal())

        val expectedResult = deckDTO.toLocal(
            courseDeck.name,
            listOf(
                kanaCardDTO.toLocal(listOf(kanaCardDTO.toLocalDTO(listOf(kanaCardDTO)))).withProgress(null),
                wordCardDTO.toLocal().withProgress(cardProgress.toLocal()),
            )
        )

        // Execute
        val result = getDeck.getDeck(learningLanguage, sourceLanguage, courseDeck, emptyList())

        // Verify
        result shouldBe expectedResult

        coVerify {
            deckRepository.getDeck(learningLanguage, sourceLanguage, courseDeck)
            cardsAndProgressRepository.getCardProgressFor(deckId)
        }
    }

    @Test
    fun `getRequiredCards should return aggregated cards from required decks`() = runBlocking {
        val requiredDeckId = DeckId("reqDeck1")
        val deckDTO = deckDTO

        coEvery { deckRepository.getDeck(learningLanguage, sourceLanguage, requiredDeckId) } returns deckDTO

        val result = getDeck.getRequiredCards(learningLanguage, sourceLanguage, listOf(requiredDeckId))

        result shouldBe listOf(wordCardDTO, kanaCardDTO)

        coVerify { deckRepository.getDeck(learningLanguage, sourceLanguage, requiredDeckId) }
    }
}