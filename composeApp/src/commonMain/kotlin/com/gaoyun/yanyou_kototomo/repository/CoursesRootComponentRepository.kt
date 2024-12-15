package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi

class CoursesRootComponentRepository(
    private val api: DecksApi,
    private val db: YanYouKotoTomoDatabase
) {

    suspend fun getCoursesRoot(): RootStructureDTO {
        val response = api.getCoursesRootComponent()
        cacheCourses(response)
        return response
    }

    private fun cacheCourses(response: RootStructureDTO) {
        response.languages.forEach { language ->
            db.coursesQueries.insertLanguage(id = language.id)
            language.sourceLanguages.forEach { sourceLanguage ->
                db.coursesQueries.insertSourceLanguage(
                    id = sourceLanguage.sourceLanguage,
                    source_language = sourceLanguage.sourceLanguage
                )
                sourceLanguage.courses.forEach { course ->
                    db.coursesQueries.insertCourse(
                        id = course.id,
                        source_language_id = sourceLanguage.sourceLanguage,
                        course_name = course.courseName,
                        required_decks = course.requiredDecks
                    )
                    course.decks.forEach { deck ->
                        db.coursesQueries.insertCourseDeck(
                            id = deck.id,
                            name = deck.name,
                            course_id = course.id,
                            type = if (deck is CourseDeckDTO.Normal) "normal" else "alphabet",
                            alphabet = if (deck is CourseDeckDTO.Alphabet) deck.alphabet else null
                        )
                    }
                }
            }
        }
    }
}