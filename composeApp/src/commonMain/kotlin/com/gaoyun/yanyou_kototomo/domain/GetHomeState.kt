package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.withProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckCourseInfo
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toCardsDTO
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.data.ui_state.HomeState
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import kotlin.collections.orEmpty

class GetHomeState(
    private val bookmarksInteractor: BookmarksInteractor,
    private val getDeckFromCache: GetDeckFromCache,
    private val getCoursesRoot: GetCoursesRoot,
    private val getCardProgress: GetCardProgress,
    private val cardsRepository: CardsAndProgressRepository,
    private val deckSettingsInteractor: DeckSettingsInteractor,
) {

    suspend fun getHomeState(): HomeState {
        val rootStructure = getCoursesRoot.getCourses()

        // Pre-build lookup maps
        val coursesWithLanguages = rootStructure.languages.flatMap { lang ->
            lang.sourceLanguages.flatMap { srcLang ->
                srcLang.courses.map { course ->
                    Triple(lang.id, srcLang.id, course)
                }
            }
        }

        // Map DeckId to DeckCourseInfo
        val deckCourseInfoMap = coursesWithLanguages.flatMap { (learningLangId, sourceLangId, course) ->
            course.decks.map { courseDeck ->
                courseDeck.id to DeckCourseInfo(
                    learningLanguageId = learningLangId,
                    sourceLanguageId = sourceLangId,
                    courseId = course.id,
                    courseName = course.courseName,
                    preview = courseDeck.preview,
                    pausedCardIds = deckSettingsInteractor.getDeckSettings(courseDeck.id)?.pausedCards ?: setOf()
                )
            }
        }.toMap()

        // Map DeckId to requiredDecks
        val courseDeckMap = coursesWithLanguages.flatMap { (_, _, course) ->
            course.decks.map { it.id to course.requiredDecks.orEmpty() }
        }.toMap()

        // Fetch bookmarked decks with course info
        val bookmarkedDecksWithInfo = bookmarksInteractor.getBookmarkedDecks().mapNotNull { bookmarkedDeck ->
            val courseInfo = deckCourseInfoMap[bookmarkedDeck.id] ?: return@mapNotNull null
            val requiredDecks = courseDeckMap[bookmarkedDeck.id] ?: emptyList()
            val deck = getDeckFromCache.getDeck(bookmarkedDeck, requiredDecks) ?: return@mapNotNull null
            deck.withInfo(courseInfo)
        }

        val recentlyReviewedCards = getRecentlyReviewedCardsWithProgress(rootStructure)

        return HomeState(
            bookmarks = bookmarkedDecksWithInfo,
            recentlyReviewed = recentlyReviewedCards
        )
    }

    private fun getRecentlyReviewedCardsWithProgress(
        rootStructure: RootStructure,
    ): List<Pair<LanguageId, CardWithProgress<Card>>> {
        // Map deck IDs to their corresponding learning language IDs
        val deckLanguageMap = rootStructure.languages.flatMap { lang ->
            lang.sourceLanguages.flatMap { srcLang ->
                srcLang.courses.flatMap { course ->
                    course.decks.map { it.id.identifier to lang.id }
                }
            }
        }.toMap()

        val courses = rootStructure.languages.flatMap { lang ->
            lang.sourceLanguages.flatMap { srcLang ->
                srcLang.courses
            }
        }

        // Map CourseDeck to required decks
        val courseDeckMap = courses.flatMap { course ->
            course.decks.map { it to course.requiredDecks.orEmpty() }
        }.toMap()

        // Fetch card progresses
        val cardsWithProgresses = getCardProgress.getCardProgressPage(0)
        val progresses = cardsWithProgresses.associateBy { it.progress.cardId }

        // Fetch recently reviewed cards
        val recentlyReviewedCards = cardsRepository.getFullCardsFromCache(cardsWithProgresses.map { it.id })
        val recentlyReviewedCardDeckIds = recentlyReviewedCards.map { it.deck_id }.toSet()

        // Fetch required decks for recently reviewed cards
        val requiredDeckIds = courseDeckMap
            .filterKeys { it.id.identifier in recentlyReviewedCardDeckIds }
            .values
            .flatten()
            .distinct()

        // Fetch required cards
        val requiredCards = requiredDeckIds
            .mapNotNull { getDeckFromCache.getDeckWithoutName(it, requiredDeckIds)?.cards }
            .flatten()
            .map { it.card }

        val requiredWords = requiredCards.filterIsInstance<Card.WordCard>()
        val kanaCards = requiredCards.filterIsInstance<Card.KanaCard>()

        // Map reviewed cards to local objects with progress
        return recentlyReviewedCards.mapNotNull { card ->
            val progress = progresses[card.id]?.progress
            val cardWithProgress = when (val cardDTO = card.toCardsDTO()) {
                is CardDTO.WordCardDTO -> cardDTO.toLocal().withProgress(progress)
                is CardDTO.KanaCardDTO -> cardDTO.toLocal(kanaCards).withProgress(progress)
                is CardDTO.KanjiCardDTO -> cardDTO.toLocal(kanaCards).withProgress(progress)
                is CardDTO.PhraseCardDTO -> cardDTO.toLocal(requiredWords).withProgress(progress)
                else -> null
            }

            // Associate the card with its LanguageId
            val languageId = deckLanguageMap[card.deck_id]
            if (languageId != null && cardWithProgress != null) {
                languageId to cardWithProgress
            } else {
                null
            }
        }
    }
}