package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.data.local.Deck
import com.gaoyun.yanyou_kototomo.ui.DeckOverviewScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.Divider
import com.gaoyun.yanyou_kototomo.ui.base.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.CardDetailsView
import moe.tlaster.precompose.koin.koinViewModel


@Composable
fun DeckOverviewScreen(
    args: DeckOverviewScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = DeckOverviewViewModel::class)
    val cardDetailState = remember { mutableStateOf<Card?>(null) }

    LaunchedEffect(Unit) {
        with(args) {
            viewModel.getCourseDecks(learningLanguageId, sourceLanguageId, courseId, deckId)
        }
    }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        DeckOverviewContent(viewModel.viewState.collectAsState().value) { cardToShow ->
            cardDetailState.value = cardToShow
        }
        CardDetailsView(cardDetailState) { cardDetailState.value = null }
    }
}

@Composable
private fun DeckOverviewContent(deck: Deck?, onCardClick: (Card) -> Unit) {
    val cellsNumber = if (deck?.isKanaDeck() == true) 5 else 2
    val cellsSpacer = if (deck?.isKanaDeck() == true) 8.dp else 16.dp

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(cellsNumber),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(cellsSpacer),
            horizontalArrangement = Arrangement.spacedBy(cellsSpacer),
            modifier = Modifier.fillMaxWidth()
        ) {
            deck?.let {
                item(span = { GridItemSpan(cellsNumber) }) {
                    AutoResizeText(
                        text = deck.name,
                        fontSizeRange = FontSizeRange(
                            max = MaterialTheme.typography.displayLarge.fontSize,
                            min = 24.sp
                        ),
                        maxLines = 1,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(end = 8.dp, bottom = 16.dp)
                    )
                }

                deck.cards.forEach { card ->
                    when (card) {
                        is Card.WordCard -> item {
                            DeckOverviewWordCard(
                                card = card,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.PhraseCard -> item {
                            DeckOverviewPhraseCard(
                                card = card,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.KanjiCard -> item {
                            DeckOverviewKanjiCard(
                                card = card,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.KanaCard -> {
                            item {
                                DeckOverviewKanaCard(
                                    card = card,
                                    onClick = { onCardClick(card) }
                                )
                            }
                            (0..<card.emptySpacesAfter()).forEach { item {} }
                        }
                    }
                }

                item(span = { GridItemSpan(cellsNumber) }) {
                    Spacer(modifier = Modifier.height(96.dp))
                }

            } ?: item(span = { GridItemSpan(cellsNumber) }) {
                CircularProgressIndicator()
            }
        }

        ElevatedButton(
            onClick = {},
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            ),
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Start learning", modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun DeckOverviewWordCard(card: Card.WordCard, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DeckCard(onClick = onClick, modifier = modifier) {
        CardFront(card.front)
        Transcription(card.transcription)
        Divider(2.dp, Modifier.padding(vertical = 4.dp))
        Translation(card.translation)
    }
}

@Composable
private fun DeckOverviewKanaCard(
    card: Card.KanaCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier, contentPadding = 0.dp) {
        CardFront(front = card.front, fontSizeMax = 48.sp)
        Transcription(
            transcription = "[${card.transcription}] ${card.mirror.front}",
            preformatted = true
        )
    }
}

@Composable
private fun DeckOverviewKanjiCard(
    card: Card.KanjiCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier) {
        CardFront(card.front, modifier = Modifier.weight(1f).padding(horizontal = 24.dp)) {
            Reading(card.reading.on)
        }
        Transcription(card.transcription)
        Divider(2.dp, Modifier.padding(vertical = 4.dp))
        Translation(card.translation)
    }
}

@Composable
private fun DeckOverviewPhraseCard(
    card: Card.PhraseCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier) {
        CardFront(card.front)
        Transcription(card.transcription)
        Divider(2.dp, Modifier.padding(vertical = 4.dp))
        Translation(card.translation)
    }
}