package com.gaoyun.yanyou_kototomo.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.BlockedBackHandler
import com.gaoyun.yanyou_kototomo.ui.base.composables.BackButtonType
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToQuizSessionSummary
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckQuizArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenMixedDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.toSealed
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerFront
import com.gaoyun.yanyou_kototomo.ui.player.components.CatAnimation
import com.gaoyun.yanyou_kototomo.ui.player.components.QuizButtons
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import com.gaoyun.yanyou_kototomo.ui.player.components.ResultAnimation
import com.gaoyun.yanyou_kototomo.ui.player.components.SpaceRepetitionButtons
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeckPlayerScreen(args: PlayerScreenDeckReviewArgs, navigate: (NavigationSideEffect) -> Unit) =
    DeckPlayerScreen(args = args.toSealed(), navigate = navigate)

@Composable
fun DeckPlayerScreen(args: PlayerScreenDeckQuizArgs, navigate: (NavigationSideEffect) -> Unit) =
    DeckPlayerScreen(args = args.toSealed(), navigate = navigate)

@Composable
fun DeckPlayerScreen(args: PlayerScreenMixedDeckReviewArgs, navigate: (NavigationSideEffect) -> Unit) =
    DeckPlayerScreen(args = args.toSealed(), navigate = navigate)

@Composable
private fun DeckPlayerScreen(
    args: PlayerScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<DeckPlayerViewModel>()

    LaunchedEffect(Unit) {
        viewModel.startPlayer(
            args = args,
            finishCallback = { sessionId ->
                when {
                    args is PlayerScreenArgs.DeckQuiz && sessionId != null -> {
                        navigate(ToQuizSessionSummary(args.toQuizSummaryArgs(sessionId), args.backToRoute))
                    }

                    else -> navigate(BackNavigationEffect)
                }
            }
        )
    }

    BlockedBackHandler()

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
        backButtonType = BackButtonType.Close,
        containerColor = if (args is PlayerScreenArgs.DeckQuiz) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceBright
        }
    ) {
        val viewState = viewModel.viewState.collectAsState().value
        AnimatedVisibility(
            visible = viewState != null,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(targetOffsetY = { it })
        ) {
            viewState?.let {
                DeckPlayerScreenContent(
                    currentCardState = viewState,
                    args = args,
                    onCardOpenClick = viewModel::openCard,
                    onNextCardClick = viewModel::nextCard,
                    onRepetitionClick = viewModel::repetitionAnswer,
                    onAnswerClick = viewModel::answerCard,
                )
            }
        }
    }
}

@Composable
private fun DeckPlayerScreenContent(
    currentCardState: PlayerCardViewState,
    args: PlayerScreenArgs,
    onCardOpenClick: () -> Unit,
    onAnswerClick: (String) -> Unit,
    onNextCardClick: () -> Unit,
    onRepetitionClick: (RepetitionAnswer, CardId) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        currentCardState.card?.let { card ->
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxHeight(fraction = 0.78f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    CardPlayerFront(card.card.front)

                    AnimatedVisibility(
                        visible = currentCardState.answerOpened,
                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                            animationSpec = tween(300),
                            initialOffsetY = { it }),
                        exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                            animationSpec = tween(300),
                            targetOffsetY = { it })
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ResultAnimation(currentCardState.answerIsCorrect)
                            when (val card = card.card) {
                                is Card.WordCard -> CardPlayerDetailsWord(card)
                                is Card.PhraseCard -> CardPlayerDetailsPhraseCard(card)
                                is Card.KanjiCard -> CardPlayerDetailsKanjiCard(card)
                                is Card.KanaCard -> CardPlayerDetailsKanaCard(card)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${currentCardState.cardNumOutOf.first}/${currentCardState.cardNumOutOf.second}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            CatAnimation(modifier = Modifier.padding(top = 24.dp))
            when (args) {
                is PlayerScreenArgs.DeckReview,
                is PlayerScreenArgs.MixedDeckReview,
                    -> SpaceRepetitionButtons(
                    currentCardState = currentCardState,
                    onCardOpenClick = onCardOpenClick,
                    onRepetitionClick = onRepetitionClick,
                )

                is PlayerScreenArgs.DeckQuiz -> QuizButtons(
                    currentCardState = currentCardState,
                    onAnswerClick = onAnswerClick,
                    onNextCardClick = onNextCardClick,
                )
            }
        }
    }
}