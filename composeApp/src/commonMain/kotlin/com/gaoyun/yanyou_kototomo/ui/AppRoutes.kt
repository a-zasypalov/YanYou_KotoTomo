package com.gaoyun.yanyou_kototomo.ui

@Suppress("FunctionName")
object AppRoutes {
    object Arg {
        const val COURSE_ID = "COURSE_ID"
        const val DECK_ID = "DECK_ID"
        const val LEARNING_LANGUAGE_ID = "LEARNING_LANGUAGE_ID"
        const val SOURCE_LANGUAGE_ID = "SOURCE_LANGUAGE_ID"
    }

    const val HOME_ROUTE = "HOME_ROUTE"
    const val PLAYER_ROUTE = "PLAYER_ROUTE"
    const val COURSES_ROUTE = "$HOME_ROUTE/COURSES_ROUTE"

    const val COURSE_DECKS_ROUTE =
        "$COURSES_ROUTE/{${Arg.LEARNING_LANGUAGE_ID}}/{${Arg.SOURCE_LANGUAGE_ID}}/{${Arg.COURSE_ID}}"

    fun COURSE_DECKS_ROUTE(args: CourseScreenArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}"

    const val DECK_OVERVIEW_ROUTE = "$COURSE_DECKS_ROUTE/{${Arg.DECK_ID}}"
    fun DECK_OVERVIEW_ROUTE(args: DeckScreenArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}/${args.deckId.identifier}"

    const val DECK_PLAYER_ROUTE = "$COURSE_DECKS_ROUTE/{${Arg.DECK_ID}}/$PLAYER_ROUTE"
    fun DECK_PLAYER_ROUTE(args: DeckScreenArgs) =
        "$COURSES_ROUTE/${args.learningLanguageId.identifier}/${args.sourceLanguageId.identifier}/${args.courseId.identifier}/${args.deckId.identifier}/$PLAYER_ROUTE"

}