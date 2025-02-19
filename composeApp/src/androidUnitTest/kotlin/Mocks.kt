import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyoukototomo.data.persistence.CardProgress
import com.gaoyun.yanyoukototomo.data.persistence.Card_progressQueries
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted
import com.gaoyun.yanyoukototomo.data.persistence.CoursesQueries
import com.gaoyun.yanyoukototomo.data.persistence.DecksQueries
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames
import io.mockk.mockk

val mockDatabase = mockk<YanYouKotoTomoDatabase>()
val mockCardProgressQueries = mockk<Card_progressQueries>()
val mockDecksQueries = mockk<DecksQueries>()
val mockCoursesQueries = mockk<CoursesQueries>()

val courseId = CourseId("course1")

val deckId = DeckId("deck1")
val alphabetTestCardId = CardId.AlphabetCardId("card1")
val wordTestCardId = CardId.AlphabetCardId("card2")
val cardIds = listOf(alphabetTestCardId, wordTestCardId)
val cardIdentifiers = cardIds.map { it.identifier }

val mockProgressList = listOf(
    CardProgress(
        card_id = "cardId",
        deck_id = deckId.identifier,
        lastReviewed = null,
        interval = null,
        easeFactor = null,
        nextReview = null,
        completed = null
    )
)

val mockCards = listOf(CardsPersisted(
    id = alphabetTestCardId.identifier,
    version = 1,
    deck_id = deckId.identifier,
    character = "あ",
    transcription = "a",
    type = "kana",
    translation = "",
    additional_info = "",
    speech_part = "noun",
    words = null,
    alphabet = "hiragana",
    mirror = "あ",
    reading_on = null,
    reading_kun = null
))
val mockDecks = listOf(GetDeckNames(deckId.identifier, "deck name"))