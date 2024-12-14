package com.gaoyun.yanyou_kototomo.data.local

sealed interface Course {
    val id: CourseId
    val courseName: String
    val decks: List<CourseDeck>

    data class Normal(
        override val id: CourseId,
        override val courseName: String,
        override val decks: List<CourseDeck>,
        val requiredDecks: List<DeckId>? = null,
    ) : Course

    data class Alphabet(
        override val id: CourseId,
        override val courseName: String,
        override val decks: List<CourseDeck>,
        val alphabet: AlphabetType
    ) : Course
}