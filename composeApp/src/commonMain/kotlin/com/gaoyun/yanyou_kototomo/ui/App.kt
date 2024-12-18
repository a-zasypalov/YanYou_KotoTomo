package com.gaoyun.yanyou_kototomo.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.AppRoutes.COURSES_ROUTE
import com.gaoyun.yanyou_kototomo.ui.AppRoutes.COURSE_DECKS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.AppRoutes.DECK_OVERVIEW_ROUTE
import com.gaoyun.yanyou_kototomo.ui.AppRoutes.DECK_PLAYER_ROUTE
import com.gaoyun.yanyou_kototomo.ui.AppRoutes.HOME_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.NavigatorAction
import com.gaoyun.yanyou_kototomo.ui.base.theme.AppTheme
import com.gaoyun.yanyou_kototomo.ui.course_decks.CourseDecksScreen
import com.gaoyun.yanyou_kototomo.ui.courses.CoursesScreen
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewScreen
import com.gaoyun.yanyou_kototomo.ui.home.HomeScreen
import com.gaoyun.yanyou_kototomo.ui.player.DeckPlayerScreen
import com.gaoyun.yanyou_kototomo.util.Platform
import com.gaoyun.yanyou_kototomo.util.PlatformNames
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
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

        AppTheme {
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                val navigator = rememberNavigator()

                LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
                    viewModel.navigationEffect.onEach { destination ->
                        when (destination) {
                            is NavigatorAction.NavigateTo -> navigator.navigate(destination.path)
                            is NavigatorAction.NavigateBack -> navigator.goBack()
                            is NavigatorAction.PopTo -> navigator.goBack(
                                PopUpTo(
                                    route = destination.path,
                                    inclusive = destination.inclusive
                                )
                            )
                        }
                    }.collect {
                        println("GlobalDestination NavigationAction: $it")
                    }
                }

                NavHost(
                    modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()),
                    navigator = navigator,
                    initialRoute = HOME_ROUTE,
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
                    scene(HOME_ROUTE) {
                        HomeScreen(viewModel::navigate)
                    }
                    scene(COURSES_ROUTE) {
                        CoursesScreen(viewModel::navigate)
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
                        it.deckScreenArgs()?.let { safeArgs ->
                            DeckPlayerScreen(args = safeArgs, navigate = viewModel::navigate)
                        }
                    }
                }
            }
        }
    }
}