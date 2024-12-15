package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.mapToRootStructureDTO
import com.gaoyun.yanyou_kototomo.data.persistence.adapters.typeToString
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi

class CoursesRootComponentRepository(
    private val api: DecksApi,
    private val db: YanYouKotoTomoDatabase
) {

    suspend fun getCoursesRoot(): RootStructureDTO {
        val cache = getCoursesFromCache()
        return cache ?: api.getCoursesRootComponent().also { cacheCourses(it) }
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
                        required_decks = course.requiredDecks
                    )

                    course.decks.forEach { deck ->
                        db.coursesQueries.insertCourseDeck(
                            id = deck.id,
                            name = deck.name,
                            course_id = course.id,
                            type = deck.typeToString(),
                            alphabet = (deck as? CourseDeckDTO.Alphabet)?.alphabet
                        )
                    }
                }
            }
        }
    }
}