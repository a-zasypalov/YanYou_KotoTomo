package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.PreviewBase
import com.gaoyun.yanyou_kototomo.ui.base.ToCourses
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit
) {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getRootComponent()
    }

    HomeScreenContent(
        navigate = { navigate(ToCourses) }
    )
}

@Composable
private fun HomeScreenContent(
    navigate: () -> Unit,
) {
    Button(onClick = navigate) {
        Text("Navigate")
    }
}

@Preview
@Composable
fun HomeScreenContentPreview() {
    PreviewBase {
        HomeScreenContent {}
    }
}