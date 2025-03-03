package com.gaoyun.yanyou_kototomo.data.local.deck

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress

data class Deck(
    val id: DeckId,
    val name: String,
    val cards: List<CardWithProgress<*>>,
) {
    fun withInfo(info: DeckCourseInfo) = DeckWithCourseInfo(this, info)

    fun getChapterName(courseName: String): String {
        return if (name.startsWith(courseName)) {
            val rest = name.removePrefix(courseName).trim()
            if (rest.isNotEmpty()) rest else courseName
        } else {
            val firstDelimiterIndex = name.indexOfAny(charArrayOf(' ', ','))
            if (firstDelimiterIndex != -1) {
                name.substring(firstDelimiterIndex).trim()
            } else {
                name
            }
        }
    }

    fun formatDeckChaptersWithLineSeparator(courseName: String): String {
        return if (name.startsWith(courseName)) {
            val rest = name.removePrefix(courseName).trim()
            if (rest.isNotEmpty()) "$courseName\n$rest" else courseName
        } else {
            val firstDelimiterIndex = name.indexOfAny(charArrayOf(' ', ','))
            if (firstDelimiterIndex != -1) {
                val firstWord = name.substring(0, firstDelimiterIndex)
                val rest = name.substring(firstDelimiterIndex).trim()
                "$firstWord\n$rest"
            } else {
                name
            }
        }
    }
}

data class DeckWithCourseInfo(
    val deck: Deck,
    val info: DeckCourseInfo,
)

data class DeckCourseInfo(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val courseName: String,
    val preview: String,
    val pausedCardIds: Set<String>,
)