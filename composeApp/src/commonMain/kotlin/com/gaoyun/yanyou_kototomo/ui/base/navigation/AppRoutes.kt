package com.gaoyun.yanyou_kototomo.ui.base.navigation

import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsListMode

@Suppress("FunctionName")
object AppRoutes {
    object Arg {
        const val COURSE_ID = "COURSE_ID"
        const val DECK_ID = "DECK_ID"
        const val BACK_TO_ROUTE = "BACK_TO_ROUTE"
        const val LEARNING_LANGUAGE_ID = "LEARNING_LANGUAGE_ID"
        const val SOURCE_LANGUAGE_ID = "SOURCE_LANGUAGE_ID"
        const val PLAYER_MODE = "PLAYER_MODE"
        const val QUIZ_SESSION_ID = "QUIZ_SESSION_ID"
        const val STATISTICS_MODE = "STATISTICS_MODE"
        const val SETTINGS_SECTION = "SETTINGS_SECTION"
    }

    const val ONBOARDING_ROUTE = "/ONBOARDING_ROUTE"
    const val HOME_HOST_ROUTE = "/HOME_HOST_ROUTE"
    const val HOME_ROUTE = "/HOME_ROUTE"
    const val COURSES_ROUTE = "/COURSES_ROUTE"
    const val STATISTICS_ROUTE = "/STATISTICS_ROUTE"
    const val SETTINGS_ROUTE = "/SETTINGS_ROUTE"

    const val COURSE_DECKS_ROUTE =
        "$COURSES_ROUTE/{${Arg.LEARNING_LANGUAGE_ID}}/{${Arg.SOURCE_LANGUAGE_ID}}/{${Arg.COURSE_ID}}"

    fun COURSE_DECKS_ROUTE(args: CourseScreenArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}"

    const val DECK_OVERVIEW_ROUTE = "$COURSE_DECKS_ROUTE/{${Arg.DECK_ID}}"
    fun DECK_OVERVIEW_ROUTE(args: DeckScreenArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}/${args.deckId.identifier}"

    const val DECK_PLAYER_ROUTE = "$DECK_OVERVIEW_ROUTE/{${Arg.PLAYER_MODE}}/{${Arg.BACK_TO_ROUTE}}"
    fun DECK_PLAYER_ROUTE(args: PlayerScreenArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}/${args.deckId.identifier}/${args.playerMode.name}/${args.backToRoute}"

    const val QUIZ_SESSION_SUMMARY_ROUTE = "$DECK_PLAYER_ROUTE/{${Arg.QUIZ_SESSION_ID}}"
    fun QUIZ_SESSION_SUMMARY_ROUTE(args: QuizSessionSummaryArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}/${args.deckId.identifier}/${args.playerMode.name}/${args.backToRoute}/${args.sessionId.identifier}"

    const val STATISTICS_FULL_ROUTE = "$STATISTICS_ROUTE/{${Arg.STATISTICS_MODE}}"
    fun STATISTICS_FULL_ROUTE(mode: StatisticsListMode) = "$STATISTICS_ROUTE/$mode"

    const val SETTINGS_SECTION_ROUTE = "$SETTINGS_ROUTE/{${Arg.SETTINGS_SECTION}}"
    fun SETTINGS_SECTION_ROUTE(section: SettingsSections) = "$SETTINGS_ROUTE/${section.name}"

}