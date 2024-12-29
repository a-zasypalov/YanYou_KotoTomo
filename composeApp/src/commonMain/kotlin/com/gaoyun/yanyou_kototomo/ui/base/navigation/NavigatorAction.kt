package com.gaoyun.yanyou_kototomo.ui.base.navigation

import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsListMode
import org.koin.core.component.KoinComponent

sealed class NavigatorAction {
    class NavigateTo(val path: String) : NavigatorAction()
    class NavigateToWithBackHandler(val path: String, val popupTo: String) : NavigatorAction()
    data object NavigateBack : NavigatorAction()
    class PopTo(val path: String, val inclusive: Boolean) : NavigatorAction()
}

interface NavigationSideEffect
object BackNavigationEffect : NavigationSideEffect

class AppNavigator() : KoinComponent {
    fun navigate(call: NavigationSideEffect): NavigatorAction? = when (call) {
        is ToCourse -> NavigatorAction.NavigateTo(AppRoutes.COURSE_DECKS_ROUTE(args = call.args))
        is ToDeck -> NavigatorAction.NavigateTo(AppRoutes.DECK_OVERVIEW_ROUTE(args = call.args))
        is ToDeckPlayer -> NavigatorAction.NavigateTo(AppRoutes.DECK_PLAYER_ROUTE(args = call.args))
        is ToStatisticsFullList -> NavigatorAction.NavigateTo(AppRoutes.STATISTICS_FULL_ROUTE(mode = call.mode))
        is ToQuizSessionSummary -> NavigatorAction.NavigateToWithBackHandler(
            path = AppRoutes.QUIZ_SESSION_SUMMARY_ROUTE(args = call.args),
            popupTo = when (call.popupTo) {
                PlayerBackRoute.Deck -> AppRoutes.DECK_OVERVIEW_ROUTE
                PlayerBackRoute.Home -> AppRoutes.HOME_HOST_ROUTE
            }
        )

        else -> null
    }
}

class ToCourse(val args: CourseScreenArgs) : NavigationSideEffect
class ToDeck(val args: DeckScreenArgs) : NavigationSideEffect
class ToDeckPlayer(val args: PlayerScreenArgs) : NavigationSideEffect
class ToQuizSessionSummary(val args: QuizSessionSummaryArgs, val popupTo: PlayerBackRoute) : NavigationSideEffect
class ToStatisticsFullList(val mode: StatisticsListMode) : NavigationSideEffect