package com.gaoyun.yanyou_kototomo.data.remote.converters

import com.gaoyun.yanyou_kototomo.data.local.Course
import com.gaoyun.yanyou_kototomo.data.local.CourseWithDecks
import com.gaoyun.yanyou_kototomo.data.local.Deck

fun Course.withDecks(decks: List<Deck>): CourseWithDecks {
    return CourseWithDecks(
        id = this.id,
        courseName = this.courseName,
        decks = decks,
        requiredDecks = decks.filter { requiredDecks?.contains(it.id) == true }
    )
}