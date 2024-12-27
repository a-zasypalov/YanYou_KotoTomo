package com.gaoyun.yanyou_kototomo.ui.statistics.full_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.statistics.StatisticsViewState
import com.gaoyun.yanyou_kototomo.ui.statistics.components.CardProgressStatisticsItem
import com.gaoyun.yanyou_kototomo.ui.statistics.components.QuizSessionStatisticsItem
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun StatisticsFullListScreen(
    mode: StatisticsListMode,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = StatisticsFullListViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.nextPage(mode)
    }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        StatisticsFullListScreenContent(
            content = viewModel.viewState.collectAsState().value,
            canRequestNewPage = viewModel.canRequestNewPage.value,
            mode = mode,
            nextPage = { viewModel.nextPage(mode) },
        )
    }
}

@Composable
private fun StatisticsFullListScreenContent(
    mode: StatisticsListMode,
    content: StatisticsViewState?,
    canRequestNewPage: Boolean,
    nextPage: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        item {
            AutoResizeText(
                text = if (mode == StatisticsListMode.Quizzes) "Quizzes" else "Spacial repetition",
                fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.displayLarge.fontSize),
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1
            )
        }
        if (!content?.sessions.isNullOrEmpty()) {
            content.sessions.forEach { session -> item { QuizSessionStatisticsItem(session) } }
        }

        if (!content?.cardsProgress.isNullOrEmpty()) {
            content.cardsProgress.forEach { cardProgress ->
                item { CardProgressStatisticsItem(cardProgress) }
            }
        }

        if (canRequestNewPage) item { LaunchedEffect(Unit) { nextPage() } }

        item { Spacer(Modifier.size(32.dp)) }
    }
}