package com.gaoyun.yanyou_kototomo.ui.base.navigation

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
) {
    fun toPlayerArgs(mode: PlayerMode): PlayerScreenArgs {
        return PlayerScreenArgs(
            learningLanguageId = learningLanguageId,
            sourceLanguageId = sourceLanguageId,
            courseId = courseId,
            deckId = deckId,
            playerMode = mode
        )
    }
}

data class PlayerScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
    val playerMode: PlayerMode
)

enum class PlayerMode {
    SpacialRepetition, Quiz
}

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

internal fun BackStackEntry.playerScreenArgs(): PlayerScreenArgs? {
    val learningLanguageId = path<String>(AppRoutes.Arg.LEARNING_LANGUAGE_ID) ?: return null
    val sourceLanguageId = path<String>(AppRoutes.Arg.SOURCE_LANGUAGE_ID) ?: return null
    val courseId = path<String>(AppRoutes.Arg.COURSE_ID) ?: return null
    val deckId = path<String>(AppRoutes.Arg.DECK_ID) ?: return null
    (path<String>(AppRoutes.Arg.PLAYER_MODE))?.let { playerMode ->
        return PlayerScreenArgs(
            learningLanguageId = LanguageId(learningLanguageId),
            sourceLanguageId = LanguageId(sourceLanguageId),
            courseId = CourseId(courseId),
            deckId = DeckId(deckId),
            playerMode = PlayerMode.valueOf(playerMode)
        )
    } ?: return null
}