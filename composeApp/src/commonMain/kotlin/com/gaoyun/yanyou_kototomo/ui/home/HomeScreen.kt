package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToCourses
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)

    SurfaceScaffold {
        HomeScreenContent(
            navigate = { navigate(ToCourses) }
        )
    }
}

@Composable
private fun HomeScreenContent(
    navigate: () -> Unit,
) {
    Button(onClick = navigate) {
        Text("Navigate")
    }
}