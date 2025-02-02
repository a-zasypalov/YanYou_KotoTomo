package com.gaoyun.yanyou_kototomo.ui.base.navigation

import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.ONBOARDING_ROUTE
import org.koin.core.component.KoinComponent

sealed class NavigatorAction {
    class NavigateToPath(val path: String) : NavigatorAction()
    class NavigateTo<T: Any>(val args: T) : NavigatorAction()
    class NavigateToPathWithBackHandler(val path: String, val popupTo: String, val inclusive: Boolean = false) : NavigatorAction()
    class NavigateToWithBackHandler<T: Any>(val args: T, val popupTo: String, val inclusive: Boolean = false) : NavigatorAction()
    data object NavigateBack : NavigatorAction()
    class PopTo(val path: String, val inclusive: Boolean) : NavigatorAction()
}

interface NavigationSideEffect
object BackNavigationEffect : NavigationSideEffect

class AppNavigator() : KoinComponent {
    fun navigate(call: NavigationSideEffect): NavigatorAction? = when (call) {
        is ToOnboarding -> NavigatorAction.NavigateToPath(ONBOARDING_ROUTE)
        is ToCourse -> NavigatorAction.NavigateTo(call.args)
        is ToDeck -> NavigatorAction.NavigateTo(call.args)
        is ToDeckPlayer -> NavigatorAction.NavigateTo(call.args)
        is ToStatisticsFullList -> NavigatorAction.NavigateTo(call.args)
        is ToSettingsSection -> NavigatorAction.NavigateTo(call.args)
        is ToQuizSessionSummary -> NavigatorAction.NavigateToWithBackHandler(
            args = call.args,
            popupTo = when (call.popupTo) {
                PlayerBackRoute.Deck -> AppRoutes.DECK_OVERVIEW_ROUTE
                PlayerBackRoute.Home -> AppRoutes.HOME_HOST_ROUTE
            }
        )

        is ToHomeScreen -> NavigatorAction.NavigateToPathWithBackHandler(
            path = AppRoutes.HOME_HOST_ROUTE,
            popupTo = ONBOARDING_ROUTE,
            inclusive = true
        )

        else -> null
    }
}

object ToHomeScreen : NavigationSideEffect
object ToOnboarding : NavigationSideEffect
class ToCourse(val args: CourseScreenArgs) : NavigationSideEffect
class ToDeck(val args: DeckScreenArgs) : NavigationSideEffect
class ToDeckPlayer(val args: PlayerScreenArgs) : NavigationSideEffect
class ToQuizSessionSummary(val args: QuizSessionSummaryArgs, val popupTo: PlayerBackRoute) : NavigationSideEffect
class ToStatisticsFullList(val args: StatisticsModeArgs) : NavigationSideEffect
class ToSettingsSection(val args: SettingsSectionsArgs) : NavigationSideEffect