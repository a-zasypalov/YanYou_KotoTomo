package com.gaoyun.yanyou_kototomo.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
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
import com.gaoyun.yanyou_kototomo.ui.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.SurfaceScaffold
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    AutoResizeText(
                        text = card.front,
                        fontSizeRange = FontSizeRange(min = 16.sp, max = 150.sp),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 150.sp
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }

                if (currentCardState.answerOpened) {
                    Text(
                        text = card.transcription,
                        style = MaterialTheme.typography.headlineSmall
                    )
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
                ElevatedButton(
                    onClick = onCardOpenClick,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Open card", modifier = Modifier.padding(8.dp))
                }

                if (!currentCardState.isLast) {
                    ElevatedButton(
                        onClick = onNextCardClick,
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Next card", modifier = Modifier.padding(8.dp))
                    }
                } else {
                    ElevatedButton(
                        onClick = onFinishClick,
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Finish", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
//
//        when (card) {
//            is Card.WordCard -> CardDetailsWord(card)
//            is Card.PhraseCard -> CardDetailsPhraseCard(card)
//            is Card.KanjiCard -> CardDetailsKanjiCard(card)
//            is Card.KanaCard -> CardDetailsKanaCard(card)
//        }
}