package com.gaoyun.yanyou_kototomo.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RootStructureDTO(
    @SerialName("languages")
    val languages: List<LearningLanguageDTO>
)

@Serializable
data class LearningLanguageDTO(
    @SerialName("id")
    val id: String,
    @SerialName("source_languages")
    val sourceLanguages: List<SourceLanguageDTO>
)

@Serializable
data class SourceLanguageDTO(
    @SerialName("source_language")
    val sourceLanguage: String,
    @SerialName("courses")
    val courses: List<CourseDTO>
)

@Serializable
data class CourseDTO(
    @SerialName("id")
    val id: String,
    @SerialName("course_name")
    val courseName: String,
    @SerialName("decks")
    val decks: List<CourseDeckDTO.Normal>,
    @SerialName("required_decks")
    val requiredDecks: List<String>? = null,
)

@Serializable
sealed interface CourseDeckDTO {
    @SerialName("id")
    val id: String

    @SerialName("name")
    val name: String

    @SerialName("normal")
    data class Normal(
        @SerialName("id")
        override val id: String,
        @SerialName("name")
        override val name: String
    ) : CourseDeckDTO

    @SerialName("alphabet")
    data class Alphabet(
        @SerialName("id")
        override val id: String,
        @SerialName("name")
        override val name: String,
        @SerialName("alphabet")
        val alphabet: String
    ) : CourseDeckDTO
}