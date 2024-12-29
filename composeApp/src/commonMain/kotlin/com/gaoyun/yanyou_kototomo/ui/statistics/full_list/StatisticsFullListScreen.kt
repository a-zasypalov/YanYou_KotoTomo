package com.gaoyun.yanyou_kototomo.ui.statistics.full_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionForStatistic
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.statistics.StatisticsViewState
import com.gaoyun.yanyou_kototomo.ui.statistics.components.CardProgressStatisticsItem
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

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
    ) {
        StatisticsFullListScreenContent(
            content = viewModel.viewState.collectAsState().value,
            canRequestNewPage = viewModel.canRequestNewPage.value,
            mode = mode,
            nextPage = { viewModel.nextPage(mode) },
            onQuizItemDelete = { viewModel.onSessionDelete(it) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StatisticsFullListScreenContent(
    mode: StatisticsListMode,
    content: StatisticsViewState?,
    canRequestNewPage: Boolean,
    nextPage: () -> Unit,
    onQuizItemDelete: (QuizSessionId) -> Unit,
) {
    val animatedSessions = remember { mutableStateListOf<QuizSessionForStatistic>() }
    LaunchedEffect(content?.sessions) {
        // Synchronize sessions
        if (content?.sessions != null) {
            val toAdd = content.sessions.filterNot { animatedSessions.contains(it) }
            val toRemove = animatedSessions.filterNot { content.sessions.contains(it) }
            animatedSessions.addAll(toAdd)
            animatedSessions.removeAll(toRemove)
        }
    }


    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        item {
            AutoResizeText(
                text = if (mode == StatisticsListMode.Quizzes) "Quizzes" else "Spacial repetition",
                fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.displayLarge.fontSize),
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (!content?.sessions.isNullOrEmpty()) {
            itemsIndexed(animatedSessions, key = { index, session -> session.sessionId.identifier }) { index, session ->
                AnimatedVisibility(
                    visible = true, // Handles fade-out when removed
                    exit = fadeOut() + shrinkVertically(),
                    modifier = Modifier.animateItemPlacement() // Smooth reordering
                ) {
                    SwipeToDismissQuizSessionItem(
                        session = session,
                        addDivider = index < content.sessions.size - 1,
                        onDelete = { onQuizItemDelete(session.sessionId) }
                    )
                }
            }
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