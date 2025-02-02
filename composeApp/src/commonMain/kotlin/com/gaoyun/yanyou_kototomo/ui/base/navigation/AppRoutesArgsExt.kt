package com.gaoyun.yanyou_kototomo.ui.base.navigation

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import kotlinx.serialization.Serializable

@Serializable
data class CourseScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
)

@Serializable
data class DeckScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
) {
    fun toPlayerArgs(mode: PlayerMode, backToRoute: PlayerBackRoute): PlayerScreenArgs {
        return PlayerScreenArgs(
            learningLanguageId = learningLanguageId,
            sourceLanguageId = sourceLanguageId,
            courseId = courseId,
            deckId = deckId,
            playerMode = mode,
            backToRoute = backToRoute
        )
    }
}

@Serializable
data class PlayerScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
    val backToRoute: PlayerBackRoute,
    val playerMode: PlayerMode,
) {
    fun toQuizSummaryArgs(sessionId: QuizSessionId): QuizSessionSummaryArgs {
        return QuizSessionSummaryArgs(
            learningLanguageId = learningLanguageId,
            sourceLanguageId = sourceLanguageId,
            courseId = courseId,
            deckId = deckId,
            playerMode = playerMode,
            backToRoute = backToRoute,
            sessionId = sessionId
        )
    }
}

@Serializable
enum class PlayerMode {
    SpacialRepetition, Quiz
}

@Serializable
enum class PlayerBackRoute {
    Home, Deck
}

@Serializable
enum class SettingsSections {
    AppIcon, ColorTheme, AboutApp, SpacialRepetition
}

@Serializable
data class QuizSessionSummaryArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
    val backToRoute: PlayerBackRoute,
    val playerMode: PlayerMode,
    val sessionId: QuizSessionId,
)