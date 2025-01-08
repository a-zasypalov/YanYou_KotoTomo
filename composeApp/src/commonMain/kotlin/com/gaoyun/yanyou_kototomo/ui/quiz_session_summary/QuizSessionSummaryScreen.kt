package com.gaoyun.yanyou_kototomo.ui.quiz_session_summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.components.QuizSessionSummaryQuestionResult
import com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.components.QuizSessionSummaryStatistics
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun QuizSessionSummaryScreen(
    args: QuizSessionSummaryArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = QuizSessionSummaryViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getSession(args)
    }

    SurfaceScaffold(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
        QuizSessionSummaryScreenContent(
            viewState = viewModel.viewState.collectAsState().value,
            onFinishClick = { navigate(BackNavigationEffect) })
    }
}

@Composable
fun QuizSessionSummaryScreenContent(
    viewState: QuizSessionSummaryViewState?,
    onFinishClick: () -> Unit,
) {
    val state = rememberLazyListState()
    viewState?.let {
        val session = it.session ?: return@let

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.weight(1f).padding(horizontal = 24.dp).padding(vertical = 24.dp)
                ) {
                    LazyColumn(
                        state = state,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            AutoResizeText(
                                text = "Quiz complete!",
                                fontSizeRange = FontSizeRange(
                                    max = MaterialTheme.typography.displayMedium.fontSize,
                                    min = 24.sp
                                ),
                                maxLines = 1,
                                style = MaterialTheme.typography.displayMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                        }
                        item { QuizSessionSummaryStatistics(session) }
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.Center) {
                                Divider(height = 2.dp, modifier = Modifier.fillMaxWidth(0.8f))
                            }
                        }
                        item {
                            Text(
                                text = "Questions",
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        itemsIndexed(session.results) { index, result ->
                            QuizSessionSummaryQuestionResult(index, session.results.lastIndex, result)
                        }
                    }
                }

                PrimaryElevatedButton(
                    text = "Finish",
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                        .padding(bottom = 8.dp),
                    onClick = onFinishClick
                )
            }
        }
    }
}