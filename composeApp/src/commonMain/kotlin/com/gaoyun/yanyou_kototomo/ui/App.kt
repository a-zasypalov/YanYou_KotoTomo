package com.gaoyun.yanyou_kototomo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.HOME_HOST_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.ONBOARDING_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigatorAction
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSections
import com.gaoyun.yanyou_kototomo.ui.base.theme.AppTheme
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColorsProvider
import com.gaoyun.yanyou_kototomo.ui.course_decks.CourseDecksScreen
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewScreen
import com.gaoyun.yanyou_kototomo.ui.home.HomeScreenHost
import com.gaoyun.yanyou_kototomo.ui.onboarding.OnboardingScreen
import com.gaoyun.yanyou_kototomo.ui.player.DeckPlayerScreen
import com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.QuizSessionSummaryScreen
import com.gaoyun.yanyou_kototomo.ui.settings.sections.SectionSettingsScreen
import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsFullListScreen
import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsListMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val viewModel = koinViewModel<AppViewModel>()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.navigationEffect.onEach { action ->
            when (action) {
                is NavigatorAction.NavigateBack -> navController.popBackStack()
                is NavigatorAction.PopTo -> navController.popBackStack(action.path, action.inclusive)
                is NavigatorAction.NavigateToPath -> navController.navigate(action.path)
                is NavigatorAction.NavigateTo<*> -> navController.navigate(action.args)
                is NavigatorAction.NavigateToWithBackHandler<*> -> {
                    navController.navigate(action.args) { popUpTo(action.popupTo) { inclusive = action.inclusive } }
                }
                is NavigatorAction.NavigateToPathWithBackHandler -> {
                    navController.navigate(action.path) { popUpTo(action.popupTo) { inclusive = action.inclusive } }
                }
            }
        }.collectLatest {}
    }

    AppTheme(viewModel.colorScheme()) {
        YanYouColorsProvider {
            Surface(
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                NavigationGraph(navController, viewModel)
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: AppViewModel) {
    NavHost(
        navController = navController,
        startDestination = viewModel.getStartRoute()
    ) {
        composable(ONBOARDING_ROUTE) {
            OnboardingScreen(viewModel::navigate)
        }
        composable(HOME_HOST_ROUTE) {
            HomeScreenHost(viewModel::navigate)
        }
        composable<CourseScreenArgs> { stackEntry ->
            CourseDecksScreen(args = stackEntry.toRoute(), navigate = viewModel::navigate)
        }
        composable<DeckScreenArgs> { stackEntry ->
            DeckOverviewScreen(args = stackEntry.toRoute(), navigate = viewModel::navigate)
        }
        composable<PlayerScreenArgs> { stackEntry ->
            DeckPlayerScreen(args = stackEntry.toRoute(), navigate = viewModel::navigate)
        }
        composable<QuizSessionSummaryArgs> { stackEntry ->
            QuizSessionSummaryScreen(args = stackEntry.toRoute(), navigate = viewModel::navigate)
        }
        composable<StatisticsListMode> { stackEntry ->
            StatisticsFullListScreen(mode = stackEntry.toRoute(), navigate = viewModel::navigate)
        }
        composable<SettingsSections> { stackEntry ->
            SectionSettingsScreen(section = stackEntry.toRoute(), navigate = viewModel::navigate)
        }
    }
}