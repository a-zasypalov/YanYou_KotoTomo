package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.CardId.AlphabetCardId
import com.gaoyun.yanyou_kototomo.data.local.CardId.PhraseCardId
import com.gaoyun.yanyou_kototomo.data.local.CardId.WordCardId

sealed interface Card {
    val id: CardId
    val front: String
    val transcription: String

    fun translationOrEmpty(prefix: String = ""): String = when(this) {
        is KanjiCard -> "$prefix$translation"
        is PhraseCard -> "$prefix$translation"
        is WordCard -> "$prefix$translation"
        is KanaCard -> ""
    }

    data class PhraseCard(
        override val id: PhraseCardId,
        override val front: String,
        override val transcription: String,
        val translation: String,
        val additionalInfo: String?,
        val words: List<WordCard>,
    ) : Card

    data class WordCard(
        override val id: WordCardId,
        override val front: String,
        override val transcription: String,
        val translation: String,
        val additionalInfo: String?,
        val speechPart: String,
    ) : Card

    data class KanaCard(
        override val id: AlphabetCardId,
        override val front: String,
        override val transcription: String,
        val alphabet: AlphabetType,
        val mirror: Mirror,
    ) : Card {
        data class Mirror(
            val id: AlphabetCardId,
            val front: String,
        )
    }

    data class KanjiCard(
        override val id: WordCardId,
        override val front: String,
        override val transcription: String,
        val reading: Reading,
        val translation: String,
        val additionalInfo: String?,
        val speechPart: String,
    ) : Card {
        data class Reading(
            val on: List<KanaCard>,
            val kun: List<KanaCard>,
        )
    }
}

fun Card.withProgress(progress: CardProgress?) = CardWithProgress(card = this, progress = progress)