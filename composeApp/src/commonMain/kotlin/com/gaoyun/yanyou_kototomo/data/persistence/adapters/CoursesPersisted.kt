package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.LearningLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.data.remote.SourceLanguageDTO
import com.gaoyun.yanyoukototomo.data.persistence.GetCourse
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
                                    preview = courseGroup.first().preview ?: "",
                                    requiredDecks = courseGroup.first().required_decks,
                                    decks = courseGroup.mapNotNull { deckRow ->
                                        when (deckRow.deck_type) {
                                            CourseDeckDTO.COURSE_DECK_NORMAL -> CourseDeckDTO.Normal(
                                                id = deckRow.deck_id
                                                    ?: error("Deck ID cannot be null"),
                                                name = deckRow.deck_name
                                                    ?: error("Deck name cannot be null"),
                                                preview = deckRow.deck_preview ?: "",
                                                version = deckRow.version?.toInt() ?: 0
                                            )

                                            CourseDeckDTO.COURSE_DECK_ALPHABET -> CourseDeckDTO.Alphabet(
                                                id = deckRow.deck_id
                                                    ?: error("Deck ID cannot be null"),
                                                name = deckRow.deck_name
                                                    ?: error("Deck name cannot be null"),
                                                preview = deckRow.deck_preview ?: "",
                                                alphabet = deckRow.alphabet
                                                    ?: error("Alphabet cannot be null for alphabet deck"),
                                                version = deckRow.version?.toInt() ?: 0
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

fun List<GetCourse>.toDTO(): CourseDTO? {
    if (isEmpty()) return null

    // Extract common course fields from the first row
    val firstRow = first()
    val courseId = firstRow.course_id
    val courseName = firstRow.course_name

    val decks = mapNotNull { row ->
        when (row.deck_type) {
            CourseDeckDTO.COURSE_DECK_NORMAL -> CourseDeckDTO.Normal(
                id = row.deck_id
                    ?: error("Deck ID cannot be null"),
                name = row.deck_name
                    ?: error("Deck name cannot be null"),
                preview = row.deck_preview ?: "",
                version = row.deck_version?.toInt() ?: 0
            )

            CourseDeckDTO.COURSE_DECK_ALPHABET -> CourseDeckDTO.Alphabet(
                id = row.deck_id
                    ?: error("Deck ID cannot be null"),
                name = row.deck_name
                    ?: error("Deck name cannot be null"),
                preview = row.deck_preview ?: "",
                version = row.deck_version?.toInt() ?: 0,
                alphabet = row.deck_alphabet
                    ?: error("Alphabet cannot be null for alphabet deck"),
            )

            else -> null // Ignore rows with unknown or null deck types
        }
    }

    return CourseDTO(
        id = courseId,
        courseName = courseName,
        preview = firstRow.preview,
        requiredDecks = firstRow.required_decks,
        decks = decks
    )
}

fun CourseDeckDTO.typeToString(): String = when (this) {
    is CourseDeckDTO.Normal -> CourseDeckDTO.COURSE_DECK_NORMAL
    is CourseDeckDTO.Alphabet -> CourseDeckDTO.COURSE_DECK_ALPHABET
}