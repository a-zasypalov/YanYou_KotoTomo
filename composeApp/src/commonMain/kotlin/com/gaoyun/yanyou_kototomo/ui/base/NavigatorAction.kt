package com.gaoyun.yanyou_kototomo.ui.base

import com.gaoyun.yanyou_kototomo.ui.AppRoutes
import com.gaoyun.yanyou_kototomo.ui.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.DeckOverviewScreenArgs
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
        is ToCourse -> NavigatorAction.NavigateTo(AppRoutes.COURSE_DECKS_ROUTE(args = call.args))
        is ToDeck -> NavigatorAction.NavigateTo(AppRoutes.DECK_OVERVIEW_ROUTE(args = call.args))

        else -> null
    }
}

object ToCourses : NavigationSideEffect
class ToCourse(val args: CourseScreenArgs) : NavigationSideEffect
class ToDeck(val args: DeckOverviewScreenArgs) : NavigationSideEffect