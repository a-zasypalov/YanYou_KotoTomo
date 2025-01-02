package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.mapToRootStructureDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.toDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.typeToString
import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.util.localDateTimeNow

class CoursesRootComponentRepository(
    private val api: DecksApi,
    private val db: YanYouKotoTomoDatabase,
    private val prefs: Preferences,
    private val deckRepository: DeckRepository,
    private val deckUpdatesRepository: DeckUpdatesRepository,
) {

    suspend fun getCoursesRoot(force: Boolean): RootStructureDTO {
        val shouldRefresh = force || deckUpdatesRepository.shouldRefreshCourses()
        val cache = if (!shouldRefresh) getCoursesFromCache() else null
        return cache ?: fetchCoursesFromApi()
    }

    suspend fun getCourse(courseId: CourseId): CourseDTO {
        val shouldRefresh = deckUpdatesRepository.shouldRefreshCourses()
        val cache = if (!shouldRefresh) {
            db.coursesQueries.getCourse(courseId.identifier).executeAsList().toDTO()
        } else null
        return cache ?: fetchCoursesFromApi().let { getCourse(courseId) }
    }

    private suspend fun fetchCoursesFromApi(): RootStructureDTO {
        return api.getCoursesRootComponent().also {
            prefs.setString(
                PreferencesKeys.UPDATES_COURSES_REFRESHED,
                localDateTimeNow().toString()
            )
            cacheCourses(it)
        }
    }

    private fun getCoursesFromCache(): RootStructureDTO? = runCatching {
        println("Getting courses from cache")
        db.coursesQueries.getRootData().executeAsList().mapToRootStructureDTO()
    }.getOrNull()

    private suspend fun cacheCourses(response: RootStructureDTO) {
        println("Caching courses from api")
        db.coursesQueries.clearCache()
        reloadDecks(response)

        response.languages.forEach { learningLanguage ->
            db.coursesQueries.insertLanguage(learningLanguage.id)
            learningLanguage.sourceLanguages.forEach { sourceLanguage ->
                db.coursesQueries.insertSourceLanguage(
                    id = learningLanguage.id,
                    source_language = sourceLanguage.sourceLanguage
                )

                sourceLanguage.courses.forEach { course ->
                    db.coursesQueries.insertCourse(
                        id = course.id,
                        language_id = learningLanguage.id,
                        source_language_id = sourceLanguage.sourceLanguage,
                        course_name = course.courseName,
                        preview = course.preview,
                        required_decks = course.requiredDecks
                    )

                    course.decks.forEach { deck ->
                        db.coursesQueries.insertCourseDeck(
                            id = deck.id,
                            name = deck.name,
                            deck_preview = deck.preview,
                            version = deck.version.toLong(),
                            course_id = course.id,
                            type = deck.typeToString(),
                            alphabet = (deck as? CourseDeckDTO.Alphabet)?.alphabet,
                        )
                    }
                }
            }
        }
    }

    private suspend fun reloadDecks(response: RootStructureDTO) {
        val responseDeckIds = response.languages.flatMap { it.sourceLanguages }.flatMap { it.courses }.flatMap { it.decks }.map { it.id }
        val downloadedDecks = db.decksQueries.getDownloadedDeckIds().executeAsList()
        val decks = db.coursesQueries.getCachedDecks().executeAsList()
            .filter { downloadedDecks.contains(it.deck_id) }
            .filter { responseDeckIds.contains(it.deck_id) }


        db.decksQueries.deleteAll()

        decks.forEach {
            deckRepository.fetchDeck(
                learningLanguage = LanguageId(it.language_id),
                sourceLanguage = LanguageId(it.source_language_id),
                deckId = DeckId(it.deck_id)
            )
        }
    }
}