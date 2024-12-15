package com.gaoyun.yanyou_kototomo.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeckDTO(
    @SerialName("deck_id")
    val id: String,
    @SerialName("version")
    val version: Int,
    @SerialName("cards")
    val cards: List<CardDTO>
)

@Serializable
sealed interface CardDTO {
    companion object {
        const val CARD_TYPE_WORD = "word"
        const val CARD_TYPE_PHRASE = "phrase"
        const val CARD_TYPE_KANA = "kana"
        const val CARD_TYPE_KANJI = "kanji"
    }

    @SerialName("id")
    val id: String

    @SerialName("character")
    val character: String

    @SerialName("transcription")
    val transcription: String

    @Serializable
    @SerialName(CARD_TYPE_WORD)
    data class WordCardDTO(
        @SerialName("id")
        override val id: String,
        @SerialName("character")
        override val character: String,
        @SerialName("transcription")
        override val transcription: String,
        @SerialName("translation")
        val translation: String,
        @SerialName("additional_info")
        val additionalInfo: String?,
        @SerialName("speech_part")
        val speechPart: String
    ) : CardDTO

    @Serializable
    @SerialName(CARD_TYPE_PHRASE)
    data class PhraseCardDTO(
        @SerialName("id")
        override val id: String,
        @SerialName("character")
        override val character: String,
        @SerialName("transcription")
        override val transcription: String,
        @SerialName("translation")
        val translation: String,
        @SerialName("additional_info")
        val additionalInfo: String?,
        @SerialName("words")
        val words: List<String>
    ) : CardDTO

    @Serializable
    @SerialName(CARD_TYPE_KANA)
    data class KanaCardDTO(
        @SerialName("id")
        override val id: String,
        @SerialName("character")
        override val character: String,
        @SerialName("transcription")
        override val transcription: String,
        @SerialName("alphabet")
        val alphabet: String,
        @SerialName("mirror")
        val mirror: String
    ) : CardDTO

    @Serializable
    @SerialName(CARD_TYPE_KANJI)
    data class KanjiCardDTO(
        @SerialName("id")
        override val id: String,
        @SerialName("character")
        override val character: String,
        @SerialName("transcription")
        override val transcription: String,
        @SerialName("reading")
        val reading: ReadingDTO,
        @SerialName("translation")
        val translation: String?,
        @SerialName("additional_info")
        val additionalInfo: String?,
        @SerialName("speech_part")
        val speechPart: String
    ) : CardDTO {

        @Serializable
        data class ReadingDTO(
            @SerialName("on")
            val on: List<String>,
            @SerialName("kun")
            val kun: List<String>
        )
    }
}