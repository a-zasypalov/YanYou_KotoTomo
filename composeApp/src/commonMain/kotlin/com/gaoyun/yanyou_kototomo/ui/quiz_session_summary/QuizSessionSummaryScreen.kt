package com.gaoyun.yanyou_kototomo.ui.quiz_session_summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.BackButtonType
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColors
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.correct
import yanyou_kototomo.composeapp.generated.resources.wrong

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
        val correctAnswers = session.results.count { it.isCorrect }
        val wrongAnswers = session.results.size - correctAnswers
        val correctAnswersPercentage = (correctAnswers.toDouble() / session.results.size.toDouble() * 100).toInt()

        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxHeight().padding(horizontal = 24.dp).padding(vertical = 24.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    )
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DataUsage, "", Modifier.size(40.dp).padding(top = 3.dp))

                        Text(
                            text = "${correctAnswersPercentage}%",
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.padding(top = 3.dp)) {
                            Text(
                                text = "$correctAnswers correct",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = YanYouColors.current.greenCorrect,
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            Divider(
                                height = 1.dp,
                                modifier = Modifier.width(56.dp).alpha(0.5f).padding(top = 2.dp).align(Alignment.CenterHorizontally)
                            )

                            Text(
                                text = "$wrongAnswers wrong",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = YanYouColors.current.redWrong,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
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
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = result.card.front,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = result.card.translationOrEmpty("– "),
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(Modifier.weight(1f))

                            if (result.isCorrect) {
                                Image(painterResource(Res.drawable.correct), "", modifier = Modifier.size(32.dp))
                            } else {
                                Image(painterResource(Res.drawable.wrong), "", modifier = Modifier.size(32.dp).padding(4.dp))
                            }
                        }

                        if (index != session.results.lastIndex) {
                            Divider(height = 1.dp)
                        } else {
                            Spacer(Modifier.size(32.dp))
                        }

                    }
                }
            }
        }
    }
}