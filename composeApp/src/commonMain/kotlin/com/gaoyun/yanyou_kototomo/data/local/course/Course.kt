package com.gaoyun.yanyou_kototomo.data.local.course

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId

data class Course(
    val id: CourseId,
    val courseName: String,
    val preview: String,
    val decks: List<CourseDeck>,
    val requiredDecks: List<DeckId>? = null,
) {
    fun withInfo(learningLanguageId: LanguageId, sourceLanguageId: LanguageId) = CourseWithInfo(
        id = id,
        learningLanguageId = learningLanguageId,
        sourceLanguageId = sourceLanguageId,
        courseName = courseName,
        preview = preview,
        decks = decks,
        requiredDecks = requiredDecks
    )
}

data class CourseWithInfo(
    val id: CourseId,
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseName: String,
    val preview: String,
    val decks: List<CourseDeck>,
    val requiredDecks: List<DeckId>? = null,
)
