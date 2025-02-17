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