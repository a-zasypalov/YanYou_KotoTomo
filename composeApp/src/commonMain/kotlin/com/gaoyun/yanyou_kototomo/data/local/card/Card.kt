package com.gaoyun.yanyou_kototomo.data.local.card

import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.CardId.AlphabetCardId
import com.gaoyun.yanyou_kototomo.data.local.CardId.PhraseCardId
import com.gaoyun.yanyou_kototomo.data.local.CardId.WordCardId

sealed interface Card {
    val id: CardId
    val front: String

    fun transcription(): String = when (this) {
        is KanjiCard -> reading.transcription()
        is PhraseCard -> transcription
        is WordCard -> transcription
        is KanaCard -> transcription
    }

    fun translationOrEmpty(prefix: String = ""): String = when (this) {
        is KanjiCard -> "$prefix$translation"
        is PhraseCard -> "$prefix$translation"
        is WordCard -> "$prefix$translation"
        is KanaCard -> ""
    }

    fun translationOrTranscription(prefix: String = ""): String = when (this) {
        is KanjiCard -> "$prefix$translation"
        is PhraseCard -> "$prefix$translation"
        is WordCard -> "$prefix$translation"
        is KanaCard -> "$prefix$transcription"
    }

    data class PhraseCard(
        override val id: PhraseCardId,
        override val front: String,
        val transcription: String,
        val translation: String,
        val additionalInfo: String?,
        val words: List<WordCard>,
    ) : Card

    data class WordCard(
        override val id: WordCardId,
        override val front: String,
        val transcription: String,
        val translation: String,
        val additionalInfo: String?,
        val speechPart: String,
    ) : Card

    data class KanaCard(
        override val id: AlphabetCardId,
        override val front: String,
        val transcription: String,
        val alphabet: AlphabetType,
        val mirror: Mirror,
        val set: Set,
    ) : Card {
        data class Mirror(
            val id: AlphabetCardId,
            val front: String,
        )

        enum class Set { Gojuon, DakuonHandakuon, Yoon }

        companion object {
            fun determineKanaSet(kana: String): Set {
                return when {
                    KanaLists.gojuonKana.contains(kana) -> Set.Gojuon
                    KanaLists.dakuonHandakuonKana.contains(kana) -> Set.DakuonHandakuon
                    KanaLists.yoonKana.contains(kana) -> Set.Yoon
                    else -> throw IllegalArgumentException("Unknown kana: $kana")
                }
            }
        }
    }

    data class KanjiCard(
        override val id: WordCardId,
        override val front: String,
        val reading: Reading,
        val translation: String,
        val additionalInfo: String?,
        val speechPart: String,
    ) : Card {
        data class Reading(
            val on: List<KanaCard>,
            val kun: List<KanaCard>,
        ) {
            fun transcription() = buildString {
                append(on.joinToString(separator = "") { it.transcription })
                append("、")
                append(kun.joinToString(separator = "") { it.transcription })
            }.toString()
        }
    }

    fun answer() = when (this) {
        is WordCard -> this.translation
        is KanaCard -> this.transcription
        is KanjiCard -> this.translation
        is PhraseCard -> this.translation
    }
}

data class CardSimpleDataEntryWithProgress(
    val id: CardId,
    val character: String,
    val answer: String,
    val progress: CardProgress,
)

data class CardSimpleDataEntry(
    val id: CardId,
    val character: String,
    val answer: String,
)

fun Card.withProgress(progress: CardProgress?) = CardWithProgress.Base(card = this, progress = progress)