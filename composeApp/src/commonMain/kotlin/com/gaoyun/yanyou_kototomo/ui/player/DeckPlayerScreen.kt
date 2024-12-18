package com.gaoyun.yanyou_kototomo.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import com.gaoyun.yanyou_kototomo.ui.base.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.Divider
import com.gaoyun.yanyou_kototomo.ui.base.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.CardDetailsAdditionalInfo
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.CardDetailsTranscription
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.CardDetailsTranslation
import moe.tlaster.precompose.koin.koinViewModel

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

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        DeckPlayerScreenContent(
            currentCardState = viewModel.viewState.collectAsState().value,
            onCardOpenClick = viewModel::openCard,
            onNextCardClick = viewModel::nextCard,
            onFinishClick = { navigate(BackNavigationEffect) }
        )
    }
}

@Composable
private fun DeckPlayerScreenContent(
    currentCardState: PlayerCardViewState?,
    onCardOpenClick: () -> Unit,
    onNextCardClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    currentCardState?.card?.let { card ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CardPlayerFront(card.front)

                if (currentCardState.answerOpened) {
                    when (card) {
                        is Card.WordCard -> CardPlayerDetailsWord(card)
                        is Card.PhraseCard -> CardPlayerDetailsPhraseCard(card)
                        is Card.KanjiCard -> CardPlayerDetailsKanjiCard(card)
                        is Card.KanaCard -> CardPlayerDetailsKanaCard(card)
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                PrimaryElevatedButton(
                    text = "Open card",
                    modifier = Modifier.weight(1f),
                    onClick = onCardOpenClick
                )

                if (!currentCardState.isLast) {
                    PrimaryElevatedButton(
                        text = "Next card",
                        modifier = Modifier.weight(1f),
                        onClick = onNextCardClick
                    )
                } else {
                    PrimaryElevatedButton(
                        text = "Finish",
                        modifier = Modifier.weight(1f),
                        onClick = onFinishClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.CardPlayerDetailsWord(card: Card.WordCard) {
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation)
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}

@Composable
private fun ColumnScope.CardPlayerDetailsKanaCard(card: Card.KanaCard) {
    CardDetailsTranscription(
        transcription = "[${card.transcription}] ${card.mirror.front}",
        preformatted = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ColumnScope.CardPlayerDetailsKanjiCard(card: Card.KanjiCard) {
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation)
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}

@Composable
private fun ColumnScope.CardPlayerDetailsPhraseCard(card: Card.PhraseCard) {
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation)
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
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
    AutoResizeText(
        text = front,
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

@Composable
internal fun BoxScope.CardPlayerReading(
    reading: List<Card.KanaCard>,
    modifier: Modifier = Modifier,
) {
    Text(
        text = reading.map { it.front }.joinToString("\n"),
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.align(Alignment.CenterEnd).padding(end = 8.dp)
    )
}