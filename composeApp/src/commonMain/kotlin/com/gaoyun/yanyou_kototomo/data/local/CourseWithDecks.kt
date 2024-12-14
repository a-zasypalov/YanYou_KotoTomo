package com.gaoyun.yanyou_kototomo.data.local

sealed interface CourseWithDecks {
    val id: CourseId
    val courseName: String
    val decks: List<Deck>

    data class Normal(
        override val id: CourseId,
        override val courseName: String,
        override val decks: List<Deck>,
        val requiredDecks: List<Deck>? = null,
    ) : CourseWithDecks

    data class Alphabet(
        override val id: CourseId,
        override val courseName: String,
        override val decks: List<Deck>,
        val alphabet: AlphabetType
    ) : CourseWithDecks
}