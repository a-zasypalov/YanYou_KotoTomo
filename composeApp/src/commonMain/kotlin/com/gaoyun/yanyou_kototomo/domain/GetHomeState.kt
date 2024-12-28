package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.withProgress
import com.gaoyun.yanyou_kototomo.data.local.course.Course
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

        val learningDeck = bookmarksInteractor.getLearningDeck()?.let { learningDeck ->
            val requiredDecks = courses.find { it.decks.contains(learningDeck) }?.requiredDecks ?: listOf()
            getDeckFromCache.getDeck(learningDeck, requiredDecks)
        }
        val bookmarkedDecks = bookmarksInteractor.getBookmarkedDecks().mapNotNull { bookmarkedDeck ->
            val requiredDecks = courses.find { it.decks.contains(bookmarkedDeck) }?.requiredDecks ?: listOf()
            getDeckFromCache.getDeck(bookmarkedDeck, requiredDecks)
        }

        val recentlyReviewedCards = getRecentlyReviewedCardsWithProgress(courses)

        return HomeState(currentlyLearn = learningDeck, bookmarks = bookmarkedDecks, recentlyReviewed = recentlyReviewedCards)

    }

    private fun getRecentlyReviewedCardsWithProgress(courses: List<Course>): List<CardWithProgress<Card>> {
        val cardsWithProgresses = getCardProgress.getCardProgressPage(0)
        val progresses = cardsWithProgresses.map { it.progress }.associateBy { it.cardId }

        val recentlyReviewedCards = cardsRepository.getFullCardsFromCache(cardsWithProgresses.map { it.id })
        val recentlyReviewedCardDeckIds = recentlyReviewedCards.map { it.deck_id }
        val recentlyReviewedCardsDto = recentlyReviewedCards.map { it.toCardsDTO() }

        val requiredDeckIds = courses
            .filter { course -> course.decks.any { it.id.identifier in recentlyReviewedCardDeckIds } }
            .flatMap { it.requiredDecks.orEmpty() }
            .distinct()

        val requiredCards = requiredDeckIds
            .mapNotNull { getDeckFromCache.getDeckWithoutName(it, requiredDeckIds) }
            .flatMap { it.cards }

        val requiredWords = requiredCards.filterIsInstance<CardDTO.WordCardDTO>()
        val kanaCards = requiredCards.filterIsInstance<CardDTO.KanaCardDTO>()

        return recentlyReviewedCardsDto.map { card ->
            val progress = progresses[card.id]

            when (card) {
                is CardDTO.WordCardDTO -> card.toLocal().withProgress(progress)
                is CardDTO.KanaCardDTO -> card.toLocal(kanaCards).withProgress(progress)
                is CardDTO.KanjiCardDTO -> card.toLocal(kanaCards).withProgress(progress)
                is CardDTO.PhraseCardDTO -> card.toLocal(requiredWords).withProgress(progress)
            }
        }
    }
}

class OptimizedGetHomeState(
    private val bookmarksInteractor: BookmarksInteractor,
    private val getDeckFromCache: GetDeckFromCache,
    private val getCoursesRoot: GetCoursesRoot,
    private val getCardProgress: GetCardProgress,
    private val cardsRepository: CardsAndProgressRepository,
) {

    suspend fun getHomeState(): HomeState? {
        val rootStructure = getCoursesRoot.getCourses()
        val courses = rootStructure.languages.flatMap { it.sourceLanguages.flatMap { l -> l.courses } }

        // Pre-build a map of decks for fast lookup
        val courseDeckMap = courses.flatMap { course -> course.decks.map { it to course.requiredDecks.orEmpty() } }.toMap()

        // Fetch the learning deck, if available
        val learningDeck = bookmarksInteractor.getLearningDeck()?.let { learningDeck ->
            val requiredDecks = courseDeckMap[learningDeck] ?: emptyList()
            getDeckFromCache.getDeck(learningDeck, requiredDecks)
        }

        // Fetch bookmarked decks
        val bookmarkedDecks = bookmarksInteractor.getBookmarkedDecks().mapNotNull { bookmarkedDeck ->
            val requiredDecks = courseDeckMap[bookmarkedDeck] ?: emptyList()
            getDeckFromCache.getDeck(bookmarkedDeck, requiredDecks)
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