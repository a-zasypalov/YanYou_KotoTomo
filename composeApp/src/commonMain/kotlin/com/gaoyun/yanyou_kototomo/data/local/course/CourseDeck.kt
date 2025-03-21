package com.gaoyun.yanyou_kototomo.data.local.course

import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import com.gaoyun.yanyou_kototomo.data.local.DeckId

sealed interface CourseDeck {
    val id: DeckId
    val name: String
    val preview: String
    val version: Int

    data class Normal(
        override val id: DeckId,
        override val name: String,
        override val preview: String,
        override val version: Int,
    ) : CourseDeck

    data class Alphabet(
        override val id: DeckId,
        override val name: String,
        override val preview: String,
        override val version: Int,
        val alphabet: AlphabetType,
    ) : CourseDeck
}