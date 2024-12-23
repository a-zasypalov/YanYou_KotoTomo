package com.gaoyun.yanyou_kototomo.data.remote.converters

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
        id = LanguageId(this.sourceLanguage),
        courses = this.courses.map { it.toLocal() }
    )
}

fun CourseDTO.toLocal(): Course {
    return Course(
        id = CourseId(this.id),
        courseName = this.courseName,
        preview = this.preview,
        decks = this.decks.map { it.toLocal() },
        requiredDecks = requiredDecks?.mapNotNull { DeckId(it) }
    )
}

fun CourseDeckDTO.toLocal(): CourseDeck {
    return when (this) {
        is CourseDeckDTO.Normal -> CourseDeck.Normal(
            id = DeckId(this.id),
            name = this.name,
            preview = this.preview,
            version = this.version,
        )

        is CourseDeckDTO.Alphabet -> {
            CourseDeck.Alphabet(
                id = DeckId(this.id),
                name = this.name,
                preview = this.preview,
                version = this.version,
                alphabet = alphabet.toAlphabet() ?: error("Wrong alphabet for $name, id:$id")
            )
        }
    }
}