package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.withProgress
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toCardsDTO
import com.gaoyun.yanyou_kototomo.data.remote.CardDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import kotlin.collections.orEmpty

class GetHomeState(
    private val bookmarksInteractor: BookmarksInteractor,
    private val getDeckFromCache: GetDeckFromCache,
    private val getCoursesRoot: GetCoursesRoot,
    private val getCardProgress: GetCardProgress,
    private val cardsRepository: CardsAndProgressRepository,
) {

    suspend fun getHomeState(): HomeState? {
        val rootStructure = getCoursesRoot.getCourses()
        val courses = rootStructure.languages.flatMap { it.sourceLanguages.flatMap { l -> l.courses } }

        // Pre-build lookup maps for fast access
        val courseDeckMap = courses.flatMap { course -> course.decks.map { it to course.requiredDecks.orEmpty() } }.toMap()
        val deckToCourseIdMap = courses.flatMap { course -> course.decks.map { it to course.id } }.toMap()

        // Fetch the learning deck, if available
        val learningDeck = bookmarksInteractor.getLearningDeck()?.let { learningDeck ->
            val courseId = deckToCourseIdMap[learningDeck] ?: CourseId("")
            val requiredDecks = courseDeckMap[learningDeck] ?: emptyList()
            courseId to getDeckFromCache.getDeck(learningDeck, requiredDecks)
        }

        // Fetch bookmarked decks
        val bookmarkedDecks = bookmarksInteractor.getBookmarkedDecks().mapNotNull { bookmarkedDeck ->
            val courseId = deckToCourseIdMap[bookmarkedDeck] ?: CourseId("")
            val requiredDecks = courseDeckMap[bookmarkedDeck] ?: emptyList()
            val deck = getDeckFromCache.getDeck(bookmarkedDeck, requiredDecks) ?: return@mapNotNull null
            courseId to deck
        }

        // Fetch recently reviewed cards with progress
        val recentlyReviewedCards = getRecentlyReviewedCardsWithProgress(courseDeckMap)

        return HomeState(
            currentlyLearn = learningDeck,
            bookmarks = bookmarkedDecks,
            recentlyReviewed = recentlyReviewedCards
        )
    }

    private fun getRecentlyReviewedCardsWithProgress(
        courseDeckMap: Map<CourseDeck, List<DeckId>>,
    ): List<CardWithProgress<Card>> {
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
            .mapNotNull { getDeckFromCache.getDeckWithoutName(it, requiredDeckIds) }
            .flatMap { it.cards }

        val requiredWords = requiredCards.filterIsInstance<CardDTO.WordCardDTO>()
        val kanaCards = requiredCards.filterIsInstance<CardDTO.KanaCardDTO>()

        // Map reviewed cards to local objects with progress
        return recentlyReviewedCards.map { card ->
            val progress = progresses[card.id]?.progress
            when (val cardDTO = card.toCardsDTO()) {
                is CardDTO.WordCardDTO -> cardDTO.toLocal().withProgress(progress)
                is CardDTO.KanaCardDTO -> cardDTO.toLocal(kanaCards).withProgress(progress)
                is CardDTO.KanjiCardDTO -> cardDTO.toLocal(kanaCards).withProgress(progress)
                is CardDTO.PhraseCardDTO -> cardDTO.toLocal(requiredWords).withProgress(progress)
            }
        }
    }
}