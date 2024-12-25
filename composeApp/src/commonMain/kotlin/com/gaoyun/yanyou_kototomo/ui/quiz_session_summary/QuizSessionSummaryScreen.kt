package com.gaoyun.yanyou_kototomo.ui.quiz_session_summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.BackButtonType
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
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

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
        backButtonType = BackButtonType.Close,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        QuizSessionSummaryScreenContent(viewModel.viewState.collectAsState().value)
    }
}

@Composable
fun QuizSessionSummaryScreenContent(viewState: QuizSessionSummaryViewState?) {
    viewState?.let {
        val session = it.session ?: return@let
        val correctAnswersPercentage = (session.results.count { it.isCorrect }.toDouble() / session.results.size.toDouble() * 100).toInt()

        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxHeight(fraction = 0.75f).padding(horizontal = 24.dp).padding(top = 24.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    AutoResizeText(
                        text = "${correctAnswersPercentage}%",
                        fontSizeRange = FontSizeRange(min = 16.sp, max = 150.sp),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 150.sp
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                items(session.results) { result ->
                    Text(result.card.front)
                    Text(if (result.isCorrect) "Correct" else "Wrong")
                }
            }
        }
    }
}