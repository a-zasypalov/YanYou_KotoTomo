package com.gaoyun.yanyou_kototomo.data.local

data class LanguageId(val identifier: String)

data class CourseId(val identifier: String)

data class DeckId(val identifier: String)

sealed interface CardId {
    val identifier: String

    data class PhraseCardId(override val identifier: String) : CardId
    data class WordCardId(override val identifier: String) : CardId
    data class AlphabetCardId(override val identifier: String) : CardId
}