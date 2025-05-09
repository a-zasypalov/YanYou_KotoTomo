package com.gaoyun.yanyou_kototomo.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageId(val identifier: String)

@Serializable
data class CourseId(val identifier: String) {
    private val kanaCourses = listOf("kana_en")
    fun isKanaCourse() = kanaCourses.contains(identifier)
}

@Serializable
data class DeckId(val identifier: String) {
    private val kanaDecks = listOf("hiragana_en", "katakana_en")
    private val mixedKanaDecks = listOf(
        "hiragana_1_en", "hiragana_2_en", "hiragana_3_en", "hiragana_4_en",
        "katakana_1_en", "katakana_2_en", "katakana_3_en", "katakana_4_en"
    )

    fun isKanaDeck() = kanaDecks.contains(identifier)
    fun isMixedKanaDeck() = mixedKanaDecks.contains(identifier)
    fun isJlptDeck() = identifier.contains("jlpt")
}

@Serializable
sealed interface CardId {
    val identifier: String

    @Serializable
    @SerialName("phrase")
    data class PhraseCardId(override val identifier: String) : CardId

    @Serializable
    @SerialName("word")
    data class WordCardId(override val identifier: String) : CardId

    @Serializable
    @SerialName("alphabet")
    data class AlphabetCardId(override val identifier: String) : CardId

    @Serializable
    @SerialName("simple_data_entry")
    data class SimpleDataEntry(override val identifier: String) : CardId
}