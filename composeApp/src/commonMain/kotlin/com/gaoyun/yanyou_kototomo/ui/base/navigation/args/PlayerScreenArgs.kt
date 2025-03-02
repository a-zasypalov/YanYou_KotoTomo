package com.gaoyun.yanyou_kototomo.ui.base.navigation.args

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerScreenDeckReviewArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckIds: List<DeckId>,
    val backToRoute: PlayerBackRoute,
)

@Serializable
data class PlayerScreenMixedDeckReviewArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckIds: List<DeckId>,
    val backToRoute: PlayerBackRoute,
)

@Serializable
data class PlayerScreenDeckQuizArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckIds: List<DeckId>,
    val backToRoute: PlayerBackRoute,
)

@Serializable
@Polymorphic
sealed interface PlayerScreenArgs {
    val learningLanguageId: LanguageId
    val sourceLanguageId: LanguageId
    val courseId: CourseId
    val backToRoute: PlayerBackRoute
    val deckIds: List<DeckId>

    interface SpacialRepetition

    @Serializable
    @SerialName("DeckReview")
    data class DeckReview(
        override val learningLanguageId: LanguageId,
        override val sourceLanguageId: LanguageId,
        override val courseId: CourseId,
        override val deckIds: List<DeckId>,
        override val backToRoute: PlayerBackRoute,
    ) : PlayerScreenArgs, SpacialRepetition

    @Serializable
    @SerialName("MixedDeckReview")
    data class MixedDeckReview(
        override val learningLanguageId: LanguageId,
        override val sourceLanguageId: LanguageId,
        override val courseId: CourseId,
        override val deckIds: List<DeckId>,
        override val backToRoute: PlayerBackRoute,
    ) : PlayerScreenArgs, SpacialRepetition

    @Serializable
    @SerialName("DeckQuiz")
    data class DeckQuiz(
        override val learningLanguageId: LanguageId,
        override val sourceLanguageId: LanguageId,
        override val courseId: CourseId,
        override val deckIds: List<DeckId>,
        override val backToRoute: PlayerBackRoute,
    ) : PlayerScreenArgs {
        fun toQuizSummaryArgs(sessionId: QuizSessionId): QuizSessionSummaryArgs {
            return QuizSessionSummaryArgs(
                learningLanguageId = learningLanguageId,
                sourceLanguageId = sourceLanguageId,
                courseId = courseId,
                deckId = deckIds.first(), //TODO: change during quiz composer implementation
                backToRoute = backToRoute,
                sessionId = sessionId
            )
        }
    }
}

@Serializable
enum class PlayerMode {
    SpacialRepetition, Quiz, MixedDeckReview
}

@Serializable
sealed class PlayerBackRoute {
    @Serializable
    object Home : PlayerBackRoute()

    @Serializable
    data class Deck(val args: DeckScreenArgs) : PlayerBackRoute()
}

fun PlayerScreenDeckReviewArgs.toSealed(): PlayerScreenArgs.DeckReview {
    return PlayerScreenArgs.DeckReview(
        learningLanguageId = learningLanguageId,
        sourceLanguageId = sourceLanguageId,
        courseId = courseId,
        deckIds = deckIds,
        backToRoute = backToRoute,
    )
}

fun PlayerScreenMixedDeckReviewArgs.toSealed(): PlayerScreenArgs.MixedDeckReview {
    return PlayerScreenArgs.MixedDeckReview(
        learningLanguageId = learningLanguageId,
        sourceLanguageId = sourceLanguageId,
        courseId = courseId,
        deckIds = deckIds,
        backToRoute = backToRoute,
    )
}

fun PlayerScreenDeckQuizArgs.toSealed(): PlayerScreenArgs.DeckQuiz {
    return PlayerScreenArgs.DeckQuiz(
        learningLanguageId = learningLanguageId,
        sourceLanguageId = sourceLanguageId,
        courseId = courseId,
        deckIds = deckIds,
        backToRoute = backToRoute,
    )
}