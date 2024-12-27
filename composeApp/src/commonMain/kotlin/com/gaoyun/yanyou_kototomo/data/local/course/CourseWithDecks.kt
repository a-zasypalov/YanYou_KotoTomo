package com.gaoyun.yanyou_kototomo.data.local.course

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck

data class CourseWithDecks(
    val id: CourseId,
    val courseName: String,
    val decks: List<Deck>,
    val requiredDecks: List<Deck>? = null,
)