package com.gaoyun.yanyou_kototomo.data.converters

import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import com.gaoyun.yanyou_kototomo.data.local.Course
import com.gaoyun.yanyou_kototomo.data.local.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.LearningLanguage
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.data.local.SourceLanguage
import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.LearningLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.data.remote.SourceLanguageDTO

fun RootStructureDTO.toLocal(): RootStructure {
    return RootStructure(
        languages = this.languages.map { it.toLocal() }
    )
}

fun LearningLanguageDTO.toLocal(): LearningLanguage {
    return LearningLanguage(
        id = LanguageId(this.id),
        sourceLanguages = this.sourceLanguages.map { it.toLocal() }
    )
}

fun SourceLanguageDTO.toLocal(): SourceLanguage {
    return SourceLanguage(
        sourceLanguage = LanguageId(this.sourceLanguage),
        courses = this.courses.map { it.toLocal() }
    )
}

fun CourseDTO.toLocal(): Course {
    return when (this) {
        is CourseDTO.Normal -> Course.Normal(
            id = CourseId(this.id),
            courseName = this.courseName,
            decks = this.decks.map { it.toLocal() },
            requiredDecks = requiredDecks?.mapNotNull { DeckId(it) }
        )

        is CourseDTO.Alphabet -> {
            val alphabet = when (this.courseName.lowercase()) {
                "hiragana" -> AlphabetType.Hiragana
                "katakana" -> AlphabetType.Katakana
                else -> throw IllegalArgumentException("Unknown alphabet type for courseName: ${this.courseName}")
            }
            Course.Alphabet(
                id = CourseId(this.id),
                courseName = this.courseName,
                decks = this.decks.map { it.toLocal() },
                alphabet = alphabet
            )
        }
    }
}

fun CourseDeckDTO.toLocal(): CourseDeck {
    return CourseDeck(
        id = DeckId(this.id),
        name = this.name,
    )
}