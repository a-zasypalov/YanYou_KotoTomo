package com.gaoyun.yanyou_kototomo.ui.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.ui.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.BackButtonType
import com.gaoyun.yanyou_kototomo.ui.base.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.Divider
import com.gaoyun.yanyou_kototomo.ui.base.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.SurfaceScaffold
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler

@Composable
fun DeckPlayerScreen(
    args: DeckScreenArgs,
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
        backButtonType = BackButtonType.Close
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
                    onCardOpenClick = viewModel::openCard,
                    onNextCardClick = viewModel::nextCard,
                    onFinishClick = { navigate(BackNavigationEffect) }
                )
            }
        }
    }
}

@Composable
private fun DeckPlayerScreenContent(
    currentCardState: PlayerCardViewState,
    onCardOpenClick: () -> Unit,
    onNextCardClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        currentCardState.card?.let { card ->
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentCardState.answerOpened,
                    transitionSpec = {
                        if (targetState) {
                            slideInVertically(animationSpec = tween(300)) { it } +
                                    fadeIn(animationSpec = tween(300)) togetherWith
                                    fadeOut(animationSpec = tween(300))
                        } else {
                            fadeIn(
                                animationSpec = tween(200, delayMillis = 300)
                            ) togetherWith fadeOut(animationSpec = tween(300))
                        }
                    },
                    label = "Answer Opened Transition",
                ) { isAnswerOpened ->
                    if (!isAnswerOpened) {
                        PrimaryElevatedButton(
                            text = "Open card",
                            modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp),
                            onClick = onCardOpenClick
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp)
                        ) {
                            PrimaryElevatedButton(
                                text = "Hard",
                                modifier = Modifier.weight(1f),
                                onClick = if (!currentCardState.isLast) onNextCardClick else onFinishClick
                            )

                            PrimaryElevatedButton(
                                text = "Good",
                                modifier = Modifier.weight(1f),
                                onClick = if (!currentCardState.isLast) onNextCardClick else onFinishClick
                            )

                            PrimaryElevatedButton(
                                text = "Easy",
                                modifier = Modifier.weight(1f),
                                onClick = if (!currentCardState.isLast) onNextCardClick else onFinishClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ColumnScope.CardPlayerDetailsWord(card: Card.WordCard) {
    CardPlayerTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
internal fun ColumnScope.CardPlayerDetailsKanaCard(card: Card.KanaCard) {
    CardPlayerTranscription(
        transcription = "[${card.transcription}] ${card.mirror.front}",
        preformatted = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
internal fun ColumnScope.CardPlayerDetailsKanjiCard(card: Card.KanjiCard) {
    CardPlayerReading(card.reading, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
internal fun ColumnScope.CardPlayerDetailsPhraseCard(card: Card.PhraseCard) {
    CardPlayerTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
internal fun CardPlayerTranscription(
    transcription: String,
    modifier: Modifier = Modifier,
    preformatted: Boolean = false,
) {
    val transcriptionFormatted = if (preformatted) transcription else "[$transcription]"
    Text(
        text = transcriptionFormatted,
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(4.dp),
    )
}

@Composable
internal fun CardPlayerTranslation(translation: String) {
    Text(
        text = translation,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
internal fun CardPlayerAdditionalInfo(info: String) {
    Text(
        text = info,
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
internal fun ColumnScope.CardPlayerFront(
    front: String,
    fontSizeMax: TextUnit = 150.sp,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = front,
        transitionSpec = {
            (fadeIn(animationSpec = tween(300)) + scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(300)
            )) togetherWith (fadeOut(animationSpec = tween(200)) + scaleOut(
                targetScale = 1.1f,
                animationSpec = tween(200)
            ))
        },
        label = "Fade Transition"
    ) { targetText ->
        AutoResizeText(
            text = targetText,
            fontSizeRange = FontSizeRange(min = 16.sp, max = fontSizeMax),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = fontSizeMax
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}

@Composable
internal fun ColumnScope.CardPlayerReading(
    reading: Card.KanjiCard.Reading,
    modifier: Modifier = Modifier,
) {
    val readingFormatted = reading.let {
        "${it.on.map { it.front }.joinToString("")}、${it.kun.map { it.front }.joinToString("")}"
    }
    val readingTranscriptionFormatted = reading.let {
        "[${it.on.map { it.transcription }.joinToString("")}, ${it.kun.map { it.transcription }.joinToString("")}]"
    }
    Text(
        text = readingFormatted,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp, top = 16.dp),
    )
    Text(
        text = readingTranscriptionFormatted,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(horizontal = 4.dp).padding(bottom = 8.dp),
    )
}