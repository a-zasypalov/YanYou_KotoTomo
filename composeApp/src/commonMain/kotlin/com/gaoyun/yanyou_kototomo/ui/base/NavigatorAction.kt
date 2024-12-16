package com.gaoyun.yanyou_kototomo.ui.base

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.ui.AppRoutes
import org.koin.core.component.KoinComponent

sealed class NavigatorAction {
    class NavigateTo(val path: String) : NavigatorAction()
    data object NavigateBack : NavigatorAction()
    class PopTo(val path: String, val inclusive: Boolean) : NavigatorAction()
}

interface NavigationSideEffect
object BackNavigationEffect : NavigationSideEffect

class AppNavigator() : KoinComponent {
    fun navigate(call: NavigationSideEffect): NavigatorAction? = when (call) {
        is ToCourses -> NavigatorAction.NavigateTo(AppRoutes.COURSES_ROUTE)
        is ToCourse -> NavigatorAction.NavigateTo(AppRoutes.COURSE_DECKS_ROUTE(call.id))
        else -> null
    }
}

object ToCourses : NavigationSideEffect
class ToCourse(val id: CourseId): NavigationSideEffect