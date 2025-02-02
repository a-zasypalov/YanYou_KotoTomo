package com.gaoyun.yanyou_kototomo.ui.base.navigation

import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.Arg

object AppRoutes {
    object Arg {
        const val COURSE_ID = "COURSE_ID"
        const val DECK_ID = "DECK_ID"
        const val LEARNING_LANGUAGE_ID = "LEARNING_LANGUAGE_ID"
        const val SOURCE_LANGUAGE_ID = "SOURCE_LANGUAGE_ID"
    }

    const val ONBOARDING_ROUTE = "/ONBOARDING_ROUTE"
    const val HOME_HOST_ROUTE = "/HOME_HOST_ROUTE"
    const val HOME_ROUTE = "/HOME_ROUTE"
    const val COURSES_ROUTE = "/COURSES_ROUTE"
    const val STATISTICS_ROUTE = "/STATISTICS_ROUTE"
    const val SETTINGS_ROUTE = "/SETTINGS_ROUTE"

    const val COURSE_DECKS_ROUTE = "$COURSES_ROUTE/{${Arg.LEARNING_LANGUAGE_ID}}/{${Arg.SOURCE_LANGUAGE_ID}}/{${Arg.COURSE_ID}}"
    const val DECK_OVERVIEW_ROUTE = "$COURSE_DECKS_ROUTE/{${Arg.DECK_ID}}"
}