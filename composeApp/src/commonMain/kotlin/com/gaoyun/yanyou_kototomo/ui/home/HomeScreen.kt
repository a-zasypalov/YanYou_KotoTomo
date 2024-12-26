package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)

    HomeScreenContent(modifier)
}

@Composable
private fun HomeScreenContent(modifier: Modifier) {

}