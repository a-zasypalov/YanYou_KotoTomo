package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.COURSES_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.HOME_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.SETTINGS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.STATISTICS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.courses.CoursesScreen
import com.gaoyun.yanyou_kototomo.ui.settings.SettingsScreen
import com.gaoyun.yanyou_kototomo.ui.statistics.StatisticsScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun HomeScreenHost(navigate: (NavigationSideEffect) -> Unit) {
    val navigator = rememberNavigator()
    val tabBarPadding = 56.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    SurfaceScaffold(bottomBar = {
        BottomNavigationBar(navigator) {
            navigator.navigate(it, NavOptions(launchSingleTop = true, popUpTo = PopUpTo(HOME_ROUTE)))
        }
    }) {
        NavHost(
            navigator = navigator,
            initialRoute = HOME_ROUTE,
            navTransition = NavTransition(
                createTransition = fadeIn(),
                destroyTransition = fadeOut(),
                pauseTransition = fadeOut(),
                resumeTransition = fadeIn(),
            )
        ) {
            scene(HOME_ROUTE) {
                HomeScreen(
                    navigate = navigate,
                    modifier = Modifier.padding(bottom = tabBarPadding),
                    onCoursesClick = { navigator.navigate(COURSES_ROUTE) }
                )
            }
            scene(STATISTICS_ROUTE) {
                StatisticsScreen(
                    navigate = navigate,
                    modifier = Modifier.padding(bottom = tabBarPadding),
                    onCoursesClick = { navigator.navigate(COURSES_ROUTE) }
                )
            }
            scene(COURSES_ROUTE) { CoursesScreen(navigate, Modifier.padding(bottom = tabBarPadding)) }
            scene(SETTINGS_ROUTE) { SettingsScreen(navigate, Modifier.padding(bottom = tabBarPadding)) }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navigator: Navigator,
    navigate: (String) -> Unit,
) {
    val currentRoute = navigator.currentEntry.collectAsState(null).value?.route?.route ?: HOME_ROUTE
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Courses,
        BottomNavItem.Statistics,
        BottomNavItem.Settings
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                onClick = { navigate(item.route) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object Home : BottomNavItem("Home", Icons.Default.Home, HOME_ROUTE)
    object Courses : BottomNavItem("Courses", Icons.Default.Book, COURSES_ROUTE)
    object Statistics : BottomNavItem("Statistics", Icons.Default.BarChart, STATISTICS_ROUTE)
    object Settings : BottomNavItem("Settings", Icons.Default.Settings, SETTINGS_ROUTE)
}