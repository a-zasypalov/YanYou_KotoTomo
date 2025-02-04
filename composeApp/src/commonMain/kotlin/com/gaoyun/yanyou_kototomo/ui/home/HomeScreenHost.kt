package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.COURSES_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.HOME_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.SETTINGS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppRoutes.STATISTICS_ROUTE
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.courses.CoursesScreen
import com.gaoyun.yanyou_kototomo.ui.settings.SettingsScreen
import com.gaoyun.yanyou_kototomo.ui.statistics.StatisticsScreen

@Composable
fun HomeScreenHost(navigate: (NavigationSideEffect) -> Unit) {
    val navController = rememberNavController()
    val tabBarPadding = 56.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    SurfaceScaffold(bottomBar = {
        BottomNavigationBar(navController) { route ->
            navController.navigate(route) {
                launchSingleTop = true
                popUpTo(HOME_ROUTE) { inclusive = false }
            }
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            enterTransition = { fadeIn(spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { fadeOut(spring(stiffness = Spring.StiffnessMedium)) },
            popEnterTransition = { fadeIn(spring(stiffness = Spring.StiffnessMedium)) },
            popExitTransition = { fadeOut(spring(stiffness = Spring.StiffnessMedium)) },
        ) {
            composable(HOME_ROUTE) {
                HomeScreen(
                    navigate = navigate,
                    modifier = Modifier.padding(bottom = tabBarPadding),
                    onCoursesClick = { navController.navigate(COURSES_ROUTE) }
                )
            }
            composable(STATISTICS_ROUTE) {
                StatisticsScreen(
                    navigate = navigate,
                    modifier = Modifier.padding(bottom = tabBarPadding),
                    onCoursesClick = { navController.navigate(COURSES_ROUTE) }
                )
            }
            composable(COURSES_ROUTE) { CoursesScreen(navigate, Modifier.padding(bottom = tabBarPadding)) }
            composable(SETTINGS_ROUTE) { SettingsScreen(navigate, Modifier.padding(bottom = tabBarPadding)) }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    navigate: (String) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: HOME_ROUTE
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Courses,
        BottomNavItem.Statistics,
        BottomNavItem.Settings
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        modifier = Modifier.shadow(
            elevation = 24.dp,
            shape = MaterialTheme.shapes.large,
            clip = true,
        ),
    ) {
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

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(HOME_ROUTE, "Home", Icons.Filled.Home)
    object Courses : BottomNavItem(COURSES_ROUTE, "Courses", Icons.Filled.Book)
    object Statistics : BottomNavItem(STATISTICS_ROUTE, "Statistics", Icons.Filled.BarChart)
    object Settings : BottomNavItem(SETTINGS_ROUTE, "Settings", Icons.Filled.Settings)
}
