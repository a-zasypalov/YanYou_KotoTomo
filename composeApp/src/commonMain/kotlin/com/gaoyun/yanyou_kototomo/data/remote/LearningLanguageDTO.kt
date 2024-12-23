package com.gaoyun.yanyou_kototomo.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RootStructureDTO(
    @SerialName("languages")
    val languages: List<LearningLanguageDTO>,
)

@Serializable
data class LearningLanguageDTO(
    @SerialName("id")
    val id: String,
    @SerialName("source_languages")
    val sourceLanguages: List<SourceLanguageDTO>,
)

@Serializable
data class SourceLanguageDTO(
    @SerialName("source_language")
    val sourceLanguage: String,
    @SerialName("courses")
    val courses: List<CourseDTO>,
)

@Serializable
data class CourseDTO(
    @SerialName("id")
    val id: String,
    @SerialName("course_name")
    val courseName: String,
    @SerialName("preview")
    val preview: String,
    @SerialName("decks")
    val decks: List<CourseDeckDTO>,
    @SerialName("required_decks")
    val requiredDecks: List<String>? = null,
)

@Serializable
sealed interface CourseDeckDTO {
    companion object {
        const val COURSE_DECK_NORMAL = "normal"
        const val COURSE_DECK_ALPHABET = "alphabet"
    }

    @SerialName("id")
    val id: String

    @SerialName("name")
    val name: String

    @SerialName("deck_preview")
    val preview: String

    @SerialName("version")
    val version: Int

    @Serializable
    @SerialName(COURSE_DECK_NORMAL)
    data class Normal(
        @SerialName("id")
        override val id: String,
        @SerialName("name")
        override val name: String,
        @SerialName("deck_preview")
        override val preview: String,
        @SerialName("version")
        override val version: Int,
    ) : CourseDeckDTO

    @Serializable
    @SerialName(COURSE_DECK_ALPHABET)
    data class Alphabet(
        @SerialName("id")
        override val id: String,
        @SerialName("name")
        override val name: String,
        @SerialName("deck_preview")
        override val preview: String,
        @SerialName("alphabet")
        val alphabet: String,
        @SerialName("version")
        override val version: Int,
    ) : CourseDeckDTO
}