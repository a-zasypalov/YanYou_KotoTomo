package com.gaoyun.yanyou_kototomo.data.local

data class CourseWithDecks(
    val id: CourseId,
    val courseName: String,
    val decks: List<Deck>,
    val requiredDecks: List<Deck>? = null,
)