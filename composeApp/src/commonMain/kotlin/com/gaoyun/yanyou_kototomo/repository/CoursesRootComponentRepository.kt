package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.CourseId
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
    private val deckUpdateRepository: DeckUpdateRepository,
) {

    suspend fun getCoursesRoot(): RootStructureDTO {
        val shouldRefresh = deckUpdateRepository.shouldRefreshCourses()
        val cache = if (!shouldRefresh) getCoursesFromCache() else null
        return cache ?: fetchCoursesFromApi()
    }

    suspend fun getCourse(courseId: CourseId): CourseDTO {
        val shouldRefresh = deckUpdateRepository.shouldRefreshCourses()
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

    private fun cacheCourses(response: RootStructureDTO) {
        println("Caching courses from api")
        db.coursesQueries.clearCache()

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
}