package com.gaoyun.yanyou_kototomo.data.local.course

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId

data class Course(
    val id: CourseId,
    val courseName: String,
    val preview: String,
    val decks: List<CourseDeck>,
    val requiredDecks: List<DeckId>? = null,
)