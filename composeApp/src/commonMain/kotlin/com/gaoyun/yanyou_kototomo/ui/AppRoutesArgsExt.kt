package com.gaoyun.yanyou_kototomo.ui

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.path

data class CourseScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
)

data class DeckScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
)

internal fun BackStackEntry.courseScreenArgs(): CourseScreenArgs? {
    val learningLanguageId = path<String>(AppRoutes.Arg.LEARNING_LANGUAGE_ID) ?: return null
    val sourceLanguageId = path<String>(AppRoutes.Arg.SOURCE_LANGUAGE_ID) ?: return null
    val courseId = path<String>(AppRoutes.Arg.COURSE_ID) ?: return null
    return CourseScreenArgs(
        learningLanguageId = LanguageId(learningLanguageId),
        sourceLanguageId = LanguageId(sourceLanguageId),
        courseId = CourseId(courseId)
    )
}

internal fun BackStackEntry.deckScreenArgs(): DeckScreenArgs? {
    val learningLanguageId = path<String>(AppRoutes.Arg.LEARNING_LANGUAGE_ID) ?: return null
    val sourceLanguageId = path<String>(AppRoutes.Arg.SOURCE_LANGUAGE_ID) ?: return null
    val courseId = path<String>(AppRoutes.Arg.COURSE_ID) ?: return null
    val deckId = path<String>(AppRoutes.Arg.DECK_ID) ?: return null
    return DeckScreenArgs(
        learningLanguageId = LanguageId(learningLanguageId),
        sourceLanguageId = LanguageId(sourceLanguageId),
        courseId = CourseId(courseId),
        deckId = DeckId(deckId)
    )
}