package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.LearningLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.data.remote.SourceLanguageDTO
import com.gaoyun.yanyoukototomo.data.persistence.GetRootData

@Throws(IllegalStateException::class)
fun List<GetRootData>.mapToRootStructureDTO(): RootStructureDTO? {
    val languages = this.groupBy { it.language_id }.map { (languageId, languageGroup) ->
        LearningLanguageDTO(
            id = languageId,
            sourceLanguages = languageGroup.groupBy { it.source_language }
                .map { (sourceLanguage, sourceLanguageGroup) ->
                    SourceLanguageDTO(
                        sourceLanguage = sourceLanguage ?: error("Source language cannot be null"),
                        courses = sourceLanguageGroup.groupBy { it.course_id }
                            .map { (courseId, courseGroup) ->
                                CourseDTO(
                                    id = courseId ?: error("Course ID cannot be null"),
                                    courseName = courseGroup.first().course_name
                                        ?: error("Course name cannot be null"),
                                    requiredDecks = courseGroup.first().required_decks,
                                    decks = courseGroup.mapNotNull { deckRow ->
                                        when (deckRow.deck_type) {
                                            CourseDeckDTO.COURSE_DECK_NORMAL -> CourseDeckDTO.Normal(
                                                id = deckRow.deck_id
                                                    ?: error("Deck ID cannot be null"),
                                                name = deckRow.deck_name
                                                    ?: error("Deck name cannot be null")
                                            )

                                            CourseDeckDTO.COURSE_DECK_ALPHABET -> CourseDeckDTO.Alphabet(
                                                id = deckRow.deck_id
                                                    ?: error("Deck ID cannot be null"),
                                                name = deckRow.deck_name
                                                    ?: error("Deck name cannot be null"),
                                                alphabet = deckRow.alphabet
                                                    ?: error("Alphabet cannot be null for alphabet deck")
                                            )

                                            else -> null // Ignore unknown deck types
                                        }
                                    }
                                )
                            }
                    )
                }
        )
    }

    return RootStructureDTO(languages = languages).takeIf { languages.isNotEmpty() }
}

fun CourseDeckDTO.typeToString(): String = when (this) {
    is CourseDeckDTO.Normal -> CourseDeckDTO.COURSE_DECK_NORMAL
    is CourseDeckDTO.Alphabet -> CourseDeckDTO.COURSE_DECK_ALPHABET
}