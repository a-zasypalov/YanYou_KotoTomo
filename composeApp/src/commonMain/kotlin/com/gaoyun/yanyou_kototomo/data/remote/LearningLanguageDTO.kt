package com.gaoyun.yanyou_kototomo.data.remote

import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import kotlinx.serialization.Serializable

@Serializable
data class RootStructureDTO(
    val languages: List<LearningLanguageDTO>
)

@Serializable
data class LearningLanguageDTO(
    val id: String,
    val sourceLanguages: List<SourceLanguageDTO>
)

@Serializable
data class SourceLanguageDTO(
    val sourceLanguage: String,
    val courses: List<CourseDTO>
)

@Serializable
sealed interface CourseDTO {
    val id: String
    val courseName: String
    val decks: List<CourseDeckDTO>

    data class Normal(
        override val id: String,
        override val courseName: String,
        override val decks: List<CourseDeckDTO>,
        val requiredDecks: List<String>? = null,
    ) : CourseDTO

    data class Alphabet(
        override val id: String,
        override val courseName: String,
        override val decks: List<CourseDeckDTO>,
        val alphabet: AlphabetType
    ) : CourseDTO
}

@Serializable
data class CourseDeckDTO(
    val id: String,
    val name: String,
)