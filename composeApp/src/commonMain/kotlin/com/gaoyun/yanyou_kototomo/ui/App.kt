package com.gaoyun.yanyou_kototomo.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.COURSE_DECKS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.DECK_OVERVIEW_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.DECK_PLAYER_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.HOME_HOST_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.QUIZ_SESSION_SUMMARY_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.SETTINGS_SECTION_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.STATISTICS_FULL_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigatorAction
import com.gaoyun.yanyou_kototomo.ui.base.navigation.courseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.deckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.playerScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.quizSessionSummaryArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.settingsSectionArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.statisticsFullListArgs
import com.gaoyun.yanyou_kototomo.ui.base.theme.AppTheme
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColorsProvider
import com.gaoyun.yanyou_kototomo.ui.course_decks.CourseDecksScreen
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewScreen
import com.gaoyun.yanyou_kototomo.ui.home.HomeScreenHost
import com.gaoyun.yanyou_kototomo.ui.player.DeckPlayerScreen
import com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.QuizSessionSummaryScreen
import com.gaoyun.yanyou_kototomo.ui.settings.sections.SectionSettingsScreen
import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsFullListScreen
import com.gaoyun.yanyou_kototomo.util.Platform
import com.gaoyun.yanyou_kototomo.util.PlatformNames
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    PreComposeApp {
        val viewModel = koinViewModel(vmClass = AppViewModel::class)

        AppTheme(viewModel.colorScheme()) {
            YanYouColorsProvider {
                Surface(
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navigator = rememberNavigator()
                    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
                        viewModel.navigationEffect.onEach { destination ->
                            when (destination) {
                                is NavigatorAction.NavigateBack -> navigator.goBack()
                                is NavigatorAction.PopTo -> navigator.goBack(PopUpTo(destination.path, destination.inclusive))
                                is NavigatorAction.NavigateTo -> navigator.navigate(destination.path)
                                is NavigatorAction.NavigateToWithBackHandler -> navigator.navigate(
                                    route = destination.path,
                                    options = NavOptions(popUpTo = PopUpTo(destination.popupTo, false))
                                )
                            }
                        }.collect {
                            println("GlobalDestination NavigationAction: $it")
                        }
                    }

                    NavHost(
                        navigator = navigator,
                        initialRoute = HOME_HOST_ROUTE,
                        swipeProperties = if (Platform.name == PlatformNames.IOS) remember {
                            SwipeProperties(
                                positionalThreshold = { distance: Float -> distance * 0.9f },
                                velocityThreshold = { 0.dp.toPx() }
                            )
                        } else null,
                        navTransition = remember {
                            NavTransition(
                                createTransition = fadeIn() + slideInHorizontally(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    ),
                                    initialOffsetX = { it }
                                ),
                                destroyTransition = fadeOut(targetAlpha = 0f) + slideOutHorizontally(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    ),
                                    targetOffsetX = { it }
                                ),
                                pauseTransition = fadeOut(targetAlpha = 0f) + slideOutHorizontally(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    ),
                                    targetOffsetX = { -it / 2 }
                                ),
                                resumeTransition = fadeIn() + slideInHorizontally(
                                    animationSpec = spring(

                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    ),
                                    initialOffsetX = { -it / 2 }
                                ),
                                exitTargetContentZIndex = 1f
                            )
                        }
                    ) {
                        scene(HOME_HOST_ROUTE) {
                            HomeScreenHost(viewModel::navigate)
                        }
                        scene(COURSE_DECKS_ROUTE) {
                            it.courseScreenArgs()?.let { safeArgs ->
                                CourseDecksScreen(args = safeArgs, navigate = viewModel::navigate)
                            }
                        }
                        scene(DECK_OVERVIEW_ROUTE) {
                            it.deckScreenArgs()?.let { safeArgs ->
                                DeckOverviewScreen(args = safeArgs, navigate = viewModel::navigate)
                            }
                        }
                        scene(DECK_PLAYER_ROUTE) {
                            it.playerScreenArgs()?.let { safeArgs ->
                                DeckPlayerScreen(args = safeArgs, navigate = viewModel::navigate)
                            }
                        }
                        scene(QUIZ_SESSION_SUMMARY_ROUTE) {
                            it.quizSessionSummaryArgs()?.let { safeArgs ->
                                QuizSessionSummaryScreen(args = safeArgs, navigate = viewModel::navigate)
                            }
                        }
                        scene(STATISTICS_FULL_ROUTE) {
                            it.statisticsFullListArgs()?.let { mode ->
                                StatisticsFullListScreen(mode = mode, navigate = viewModel::navigate)
                            }
                        }
                        scene(SETTINGS_SECTION_ROUTE) {
                            it.settingsSectionArgs()?.let { section ->
                                SectionSettingsScreen(section = section, navigate = viewModel::navigate)
                            }
                        }
                    }
                }
            }
        }
    }
}