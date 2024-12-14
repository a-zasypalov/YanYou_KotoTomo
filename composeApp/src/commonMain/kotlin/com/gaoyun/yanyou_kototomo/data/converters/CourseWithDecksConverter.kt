package com.gaoyun.yanyou_kototomo.data.converters

import com.gaoyun.yanyou_kototomo.data.local.Course
import com.gaoyun.yanyou_kototomo.data.local.CourseWithDecks
import com.gaoyun.yanyou_kototomo.data.local.Deck

fun Course.Normal.withDecks(decks: List<Deck>): CourseWithDecks {
    return CourseWithDecks.Normal(
        id = this.id,
        courseName = this.courseName,
        decks = decks,
        requiredDecks = decks.filter { requiredDecks?.contains(it.id) == true }
    )
}

fun Course.Alphabet.withDecks(decks: List<Deck>): CourseWithDecks {
    return CourseWithDecks.Alphabet(
        id = this.id,
        courseName = this.courseName,
        decks = decks,
        alphabet = this.alphabet,
    )
}