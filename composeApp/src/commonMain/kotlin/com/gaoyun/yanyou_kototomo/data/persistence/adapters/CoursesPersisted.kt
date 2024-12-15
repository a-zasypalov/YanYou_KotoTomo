package com.gaoyun.yanyou_kototomo.data.persistence.adapters

import com.gaoyun.yanyou_kototomo.data.remote.CourseDTO
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.LearningLanguageDTO
import com.gaoyun.yanyou_kototomo.data.remote.SourceLanguageDTO
import com.gaoyun.yanyoukototomo.data.persistence.CourseDecksPersisted
import com.gaoyun.yanyoukototomo.data.persistence.CoursesPersisted
import com.gaoyun.yanyoukototomo.data.persistence.LanguagesPersisted
import com.gaoyun.yanyoukototomo.data.persistence.SourceLanguagesPersisted

fun LanguagesPersisted.toLearningLanguageDTO(sourceLanguages: List<SourceLanguagesPersisted>): LearningLanguageDTO {
    val mappedSourceLanguages = sourceLanguages.map {
        SourceLanguageDTO(
            sourceLanguage = it.source_language,
            courses = emptyList() // Populate courses separately
        )
    }
    return LearningLanguageDTO(id = id, sourceLanguages = mappedSourceLanguages)
}

fun CourseDecksPersisted.toCourseDeckDTO(): CourseDeckDTO {
    return when (type) {
        "normal" -> CourseDeckDTO.Normal(id = id, name = name)
        "alphabet" -> CourseDeckDTO.Alphabet(id = id, name = name, alphabet = alphabet!!)
        else -> throw IllegalArgumentException("Unknown deck type: $type")
    }
}

fun CoursesPersisted.toCourseDTO(decks: List<CourseDeckDTO>): CourseDTO {
    return CourseDTO(
        id = id,
        courseName = course_name,
        decks = decks,
        requiredDecks = required_decks
    )
}