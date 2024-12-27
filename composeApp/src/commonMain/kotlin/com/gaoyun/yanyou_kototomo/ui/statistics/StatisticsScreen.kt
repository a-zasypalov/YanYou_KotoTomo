package com.gaoyun.yanyou_kototomo.ui.statistics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun StatisticsScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = StatisticsViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getStatistics()
    }

    StatisticsScreenContent(modifier = modifier)
}

@Composable
private fun StatisticsScreenContent(
    modifier: Modifier,
) {

}