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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.BackButtonType
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerScreenArgs
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerAdditionalInfo
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerFront
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerReading
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerTranscription
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerTranslation
import com.gaoyun.yanyou_kototomo.ui.player.components.QuizButtons
import com.gaoyun.yanyou_kototomo.ui.player.components.SpaceRepetitionButtons
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler

@Composable
fun DeckPlayerScreen(
    args: PlayerScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = DeckPlayerViewModel::class)

    LaunchedEffect(Unit) {
        with(args) {
            viewModel.startPlayer(learningLanguageId, sourceLanguageId, courseId, deckId)
        }
    }

    BackHandler {}

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
        backButtonType = BackButtonType.Close,
        containerColor = if (args.playerMode == PlayerMode.Quiz) {
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
                    mode = args.playerMode,
                    onCardOpenClick = viewModel::openCard,
                    onNextCardClick = viewModel::nextCard,
                    onAnswerClick = viewModel::answerCard,
                    onFinishClick = { navigate(BackNavigationEffect) }
                )
            }
        }
    }
}

@Composable
private fun DeckPlayerScreenContent(
    currentCardState: PlayerCardViewState,
    mode: PlayerMode,
    onCardOpenClick: () -> Unit,
    onAnswerClick: (String) -> Unit,
    onNextCardClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        currentCardState.card?.let { card ->
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxHeight(fraction = 0.75f).padding(horizontal = 24.dp).padding(top = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    CardPlayerFront(card.front)

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
                            when (card) {
                                is Card.WordCard -> CardPlayerDetailsWord(card)
                                is Card.PhraseCard -> CardPlayerDetailsPhraseCard(card)
                                is Card.KanjiCard -> CardPlayerDetailsKanjiCard(card)
                                is Card.KanaCard -> CardPlayerDetailsKanaCard(card)
                            }
                        }
                    }
                }
            }
            when (mode) {
                PlayerMode.SpacialRepetition -> SpaceRepetitionButtons(
                    currentCardState = currentCardState,
                    onCardOpenClick = onCardOpenClick,
                    onNextCardClick = onNextCardClick,
                    onFinishClick = onFinishClick
                )

                PlayerMode.Quiz -> QuizButtons(
                    currentCardState = currentCardState,
                    onAnswerClick = onAnswerClick,
                    onNextCardClick = onNextCardClick,
                    onFinishClick = onFinishClick
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.CardPlayerDetailsWord(card: Card.WordCard) {
    CardPlayerTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
private fun ColumnScope.CardPlayerDetailsKanaCard(card: Card.KanaCard) {
    CardPlayerTranscription(
        transcription = "[${card.transcription}] ${card.mirror.front}",
        preformatted = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ColumnScope.CardPlayerDetailsKanjiCard(card: Card.KanjiCard) {
    CardPlayerReading(card.reading, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
private fun ColumnScope.CardPlayerDetailsPhraseCard(card: Card.PhraseCard) {
    CardPlayerTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}