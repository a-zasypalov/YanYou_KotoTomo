package com.gaoyun.yanyou_kototomo.data.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class LanguageId(val identifier: String)

data class CourseId(val identifier: String)

data class DeckId(val identifier: String)

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