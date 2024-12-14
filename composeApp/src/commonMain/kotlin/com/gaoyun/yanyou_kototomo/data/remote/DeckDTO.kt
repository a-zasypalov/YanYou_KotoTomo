package com.gaoyun.yanyou_kototomo.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeckDTO(
    val id: String,
    val cards: List<CardDTO>
)

@Serializable
sealed interface CardDTO {
    val id: String
    val front: String
    val transcription: String

    @Serializable
    @SerialName("phrase")
    data class PhraseCardDTO(
        override val id: String,
        override val front: String,
        override val transcription: String,
        val translation: String,
        val additionalInfo: String?,
        val words: List<String>
    ) : CardDTO

    @Serializable
    @SerialName("word")
    data class WordCardDTO(
        override val id: String,
        override val front: String,
        override val transcription: String,
        val translation: String,
        val additionalInfo: String?,
        val speechPart: String
    ) : CardDTO

    @Serializable
    @SerialName("kana")
    data class KanaCardDTO(
        override val id: String,
        override val front: String,
        override val transcription: String,
        val alphabet: String,
        val mirror: String
    ) : CardDTO

    @Serializable
    @SerialName("kanji")
    data class KanjiCardDTO(
        override val id: String,
        override val front: String,
        override val transcription: String,
        val reading: ReadingDTO,
        val translation: String?,
        val additionalInfo: String?,
        val speechPart: String
    ) : CardDTO {

        @Serializable
        data class ReadingDTO(
            val on: List<String>,
            val kun: List<String>
        )
    }
}