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
import com.gaoyun.yanyou_kototomo.ui.AppRoutes.HOME_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.theme.AppTheme
import com.gaoyun.yanyou_kototomo.ui.home.HomeScreen
import com.gaoyun.yanyou_kototomo.util.Platform
import com.gaoyun.yanyou_kototomo.util.PlatformNames
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    PreComposeApp {
        AppTheme {
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                val navigator = rememberNavigator()
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
                    navTransition = if (Platform.name == PlatformNames.IOS) {
                        remember {
                            NavTransition(
                                createTransition = fadeIn() + slideInHorizontally(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    ),
                                    initialOffsetX = { it }
                                ),
                                destroyTransition = fadeOut(targetAlpha = 0.5f) + slideOutHorizontally(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    ),
                                    targetOffsetX = { it }
                                ),
                                pauseTransition = fadeOut(targetAlpha = 0.5f) + slideOutHorizontally(
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
                    } else NavTransition()
                ) {
                    scene(HOME_ROUTE) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}