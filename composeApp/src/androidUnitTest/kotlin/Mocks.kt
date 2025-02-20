import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckCourseInfo
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.LearningLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.data.remote.SourceLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.util.localDateNow
import com.gaoyun.yanyoukototomo.data.persistence.CardProgress
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted
import com.gaoyun.yanyoukototomo.data.persistence.GetCourse
import com.gaoyun.yanyoukototomo.data.persistence.GetDeckNames

// Mock IDs
val courseId = CourseId("course1")
val deckId = DeckId("deck1")
val alphabetTestCardId = CardId.AlphabetCardId("wordCardId")
val kanaTestCardId = CardId.AlphabetCardId("kanaCardId1")
val cardIds = listOf(alphabetTestCardId, kanaTestCardId)
val cardIdentifiers = cardIds.map { it.identifier }


// Mock Card Progress
val cardProgress = CardProgress(
    card_id = alphabetTestCardId.identifier,
    deck_id = deckId.identifier,
    lastReviewed = localDateNow().toString(),
    interval = 1,
    easeFactor = 2.0,
    nextReview = localDateNow().toString(),
    completed = false
)
val mockProgressList = listOf(cardProgress)

// Mock Cards Persisted
val mockCards = listOf(
    CardsPersisted(
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
    ),
    CardsPersisted(
        id = kanaTestCardId.identifier,
        version = 1,
        deck_id = deckId.identifier,
        character = "かな",
        transcription = "kana1",
        type = "kana",
        translation = "",
        additional_info = "",
        speech_part = "noun",
        words = null,
        alphabet = "hiragana",
        mirror = "kana11",
        reading_on = null,
        reading_kun = null
    )
)

// Mock Deck Names
val mockDecks = listOf(GetDeckNames(deckId.identifier, "deck name"))

// Mock GetCourse
val mockGetCourse = GetCourse(
    course_id = courseId.identifier,
    course_name = "Course 1",
    preview = "Preview of Course 1",
    required_decks = listOf(deckId.identifier, "deck2"),
    deck_id = deckId.identifier,
    deck_name = "Deck 1",
    deck_preview = "Preview of Deck 1",
    deck_version = 1L,
    deck_type = "Alphabet",
    deck_alphabet = "hiragana"
)

// Mock Course Deck DTO
val courseDeckDto = CourseDeckDTO.Alphabet(
    id = deckId.identifier,
    name = "Deck 1",
    preview = "Preview",
    version = 1,
    alphabet = "hiragana"
)
val courseDeck = courseDeckDto.toLocal()

// Mock Course DTO
val courseDto = CourseDTO(
    id = courseId.identifier,
    courseName = "Course 1",
    preview = "Preview",
    requiredDecks = listOf(deckId.identifier),
    decks = listOf(courseDeckDto)
)

// Mock Root Structure DTO
val rootStructureDTO = RootStructureDTO(
    languages = listOf(
        LearningLanguageDTO(
            id = "cn",
            sourceLanguages = listOf(
                SourceLanguageDTO(
                    sourceLanguage = "en",
                    courses = listOf(courseDto)
                )
            )
        )
    )
)

// Mock Course Info
val courseInfo = DeckCourseInfo(
    learningLanguageId = LanguageId("l1"),
    sourceLanguageId = LanguageId("l2"),
    courseId = CourseId("c1"),
    courseName = "course 1",
    preview = "preview",
    pausedCardIds = setOf()
)

// Mock Deck with Course Info
val deckWithCourseInfo = DeckWithCourseInfo(
    deck = Deck(
        id = DeckId("deck1"),
        name = "Deck 1",
        cards = listOf()
    ),
    info = courseInfo
)

// Mock CardDTO Objects
val wordCardDTO = CardDTO.WordCardDTO(
    id = alphabetTestCardId.identifier,
    character = "word1",
    transcription = "transcription1",
    translation = "translation1",
    additionalInfo = "",
    speechPart = "noun"
)

val kanaCardDTO = CardDTO.KanaCardDTO(
    id = kanaTestCardId.identifier,
    character = "あ",
    transcription = "a",
    alphabet = "hiragana",
    mirror = kanaTestCardId.identifier
)

// Mock Deck DTO
val deckDTO = DeckDTO(
    id = deckId.identifier,
    version = 1,
    cards = listOf(wordCardDTO, kanaCardDTO)
)