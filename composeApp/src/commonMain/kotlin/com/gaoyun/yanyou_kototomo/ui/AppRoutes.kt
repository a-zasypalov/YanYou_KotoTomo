package com.gaoyun.yanyou_kototomo.ui

import com.gaoyun.yanyou_kototomo.data.local.CourseId

@Suppress("FunctionName")
object AppRoutes {
    object Arg {
        const val COURSE_ID = "COURSE_ID"
    }

    const val HOME_ROUTE = "HOME_ROUTE"
    const val COURSES_ROUTE = "$HOME_ROUTE/COURSES_ROUTE"

    const val COURSE_DECKS_ROUTE = "$COURSES_ROUTE/{${Arg.COURSE_ID}}"
    fun COURSE_DECKS_ROUTE(courseId: CourseId) = "$COURSES_ROUTE/${courseId.identifier}"
}