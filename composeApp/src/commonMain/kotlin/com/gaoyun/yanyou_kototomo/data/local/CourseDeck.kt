package com.gaoyun.yanyou_kototomo.data.local

sealed interface CourseDeck {
    val id: DeckId
    val name: String

    data class Normal(
        override val id: DeckId,
        override val name: String
    ) : CourseDeck

    data class Alphabet(
        override val id: DeckId,
        override val name: String,
        val alphabet: AlphabetType
    ) : CourseDeck
}