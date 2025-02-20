package domain

import cardProgress
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.card.CardSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toLocal
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.domain.GetCardProgress
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*
import org.junit.jupiter.api.Test

class GetCardProgressTest {

    private val repository: CardsAndProgressRepository = mockk()
    private val getCardProgress: GetCardProgress = GetCardProgress(repository = repository)

    @Test
    fun `getCardProgressPage should return correct progress entry list`() {
        val page = 1
        val cardProgresses = listOf(cardProgress.toLocal())
        val cards = listOf(cardDto to getDeckNames)

        every { repository.getCardProgressPage(page) } returns cardProgresses
        every { repository.getCards(cardProgresses.map { it.cardId }) } returns cards

        val expectedSimpleDataWithProgress = CardSimpleDataEntryWithProgress(
            id = CardId.SimpleDataEntry(cardProgress.card_id),
            character = "あ",
            answer = "a",
            progress = cardProgress.toLocal()
        )

        val result = getCardProgress.getCardProgressPage(page)

        result.shouldContainExactly(expectedSimpleDataWithProgress)

        // Verify repository interactions
        verify { repository.getCardProgressPage(page) }
        verify { repository.getCards(listOf(cardProgress.toLocal().cardId)) }
    }
}

val cardDto = CardDTO.KanaCardDTO(
    id = cardProgress.card_id,
    character = "あ",
    transcription = "a",
    alphabet = "hiragana",
    mirror = "a",
)

val getDeckNames = GetDeckNames(id = cardProgress.deck_id, name = "name")