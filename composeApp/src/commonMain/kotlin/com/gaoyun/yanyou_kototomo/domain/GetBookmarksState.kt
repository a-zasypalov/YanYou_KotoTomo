package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.deck.DeckCourseInfo
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import kotlin.collections.orEmpty

class GetBookmarksState(
    private val bookmarksInteractor: BookmarksInteractor,
    private val getDeckFromCache: GetDeckFromCache,
    private val getCoursesRoot: GetCoursesRoot,
    private val deckSettingsInteractor: DeckSettingsInteractor,
) {

    suspend fun get(): List<DeckWithCourseInfo> {
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


        return bookmarkedDecksWithInfo
    }
}