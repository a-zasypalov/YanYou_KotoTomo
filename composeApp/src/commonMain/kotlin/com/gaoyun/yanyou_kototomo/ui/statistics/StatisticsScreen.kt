package com.gaoyun.yanyou_kototomo.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.statistics.components.CardProgressStatisticsItem
import com.gaoyun.yanyou_kototomo.ui.statistics.components.QuizSessionStatisticsItem
import com.gaoyun.yanyou_kototomo.ui.statistics.components.SectionDividerShowMore
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

    StatisticsScreenContent(
        content = viewModel.viewState.collectAsState().value,
        modifier = modifier,
        onShowMoreCards = {},
        onShowMoreQuizzes = {}
    )
}

@Composable
private fun StatisticsScreenContent(
    content: StatisticsViewState?,
    modifier: Modifier,
    onShowMoreQuizzes: () -> Unit,
    onShowMoreCards: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        item {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        if (!content?.sessions.isNullOrEmpty()) {
            item {
                Text(
                    text = "Quizzes",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            content.sessions.take(5).forEach { session -> item { QuizSessionStatisticsItem(session) } }
            item { SectionDividerShowMore(onShowMoreQuizzes) }
        }

        if (!content?.cardsProgress.isNullOrEmpty()) {
            item {
                Text(
                    text = "Spacial repetition",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            content.cardsProgress.sortedBy { it.progress.nextReview }.take(10).forEach { cardProgress ->
                item { CardProgressStatisticsItem(cardProgress) }
            }
            item { SectionDividerShowMore(onShowMoreCards) }
        }

        item { Spacer(Modifier.size(32.dp)) }
    }
}