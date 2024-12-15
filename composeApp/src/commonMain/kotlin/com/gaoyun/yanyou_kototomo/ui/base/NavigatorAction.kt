package com.gaoyun.yanyou_kototomo.ui.base

import com.gaoyun.yanyou_kototomo.ui.AppRoutes
import com.gaoyun.yanyou_kototomo.ui.home.ToCourses
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
        ToCourses -> NavigatorAction.NavigateTo(AppRoutes.COURSES_ROUTE)
        else -> null
    }
}