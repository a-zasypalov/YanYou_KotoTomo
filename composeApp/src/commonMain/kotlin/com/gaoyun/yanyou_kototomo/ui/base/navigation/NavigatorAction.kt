package com.gaoyun.yanyou_kototomo.ui.base.navigation

import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.BOOKMARKS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.ONBOARDING_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckQuizArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenMixedDeckReviewArgs
import org.koin.core.component.KoinComponent

sealed class NavigatorAction {
    class NavigateToPath(val path: String) : NavigatorAction()
    class NavigateTo<T : Any>(val args: T) : NavigatorAction()
    class NavigateToPathWithBackHandler(val path: String, val popupTo: String, val inclusive: Boolean = false) : NavigatorAction()
    class NavigateToWithBackHandler<T : Any, P : Any>(val args: T, val popupTo: P, val inclusive: Boolean = false) : NavigatorAction()
    class NavigateToWithPathBackHandler<T : Any>(val args: T, val popupTo: String, val inclusive: Boolean = false) : NavigatorAction()
    data object NavigateBack : NavigatorAction()
    class PopTo(val path: String, val inclusive: Boolean) : NavigatorAction()
}

interface NavigationSideEffect
object BackNavigationEffect : NavigationSideEffect

class AppNavigator() : KoinComponent {
    fun navigate(call: NavigationSideEffect): NavigatorAction? = when (call) {
        is ToOnboarding -> NavigatorAction.NavigateToPath(ONBOARDING_ROUTE)
        is ToBookmarks -> NavigatorAction.NavigateToPath(BOOKMARKS_ROUTE)
        is ToCourse -> NavigatorAction.NavigateTo(call.args)
        is ToDeck -> NavigatorAction.NavigateTo(call.args)
        is ToDeckReviewPlayer -> NavigatorAction.NavigateTo(call.args)
        is ToDeckQuizPlayer -> NavigatorAction.NavigateTo(call.args)
        is ToMixedDeckReviewPlayer -> NavigatorAction.NavigateTo(call.args)
        is ToStatisticsFullList -> NavigatorAction.NavigateTo(call.args)
        is ToSettingsSection -> NavigatorAction.NavigateTo(call.args)
        is ToQuizSessionSummary -> when (call.popupTo) {
            is PlayerBackRoute.Deck -> NavigatorAction.NavigateToWithBackHandler(
                args = call.args,
                popupTo = call.popupTo.args
            )

            PlayerBackRoute.Home -> NavigatorAction.NavigateToWithPathBackHandler(
                args = call.args,
                popupTo = AppRoutes.HOME_HOST_ROUTE
            )
        }

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
object ToBookmarks : NavigationSideEffect
class ToCourse(val args: CourseScreenArgs) : NavigationSideEffect
class ToDeck(val args: DeckScreenArgs) : NavigationSideEffect
class ToDeckReviewPlayer(val args: PlayerScreenDeckReviewArgs) : NavigationSideEffect
class ToDeckQuizPlayer(val args: PlayerScreenDeckQuizArgs) : NavigationSideEffect
class ToMixedDeckReviewPlayer(val args: PlayerScreenMixedDeckReviewArgs) : NavigationSideEffect
class ToQuizSessionSummary(val args: QuizSessionSummaryArgs, val popupTo: PlayerBackRoute) : NavigationSideEffect
class ToStatisticsFullList(val args: StatisticsModeArgs) : NavigationSideEffect
class ToSettingsSection(val args: SettingsSectionsArgs) : NavigationSideEffect
