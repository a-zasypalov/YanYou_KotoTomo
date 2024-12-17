package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun DeckOverviewScreen(
    args: DeckOverviewScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = DeckOverviewViewModel::class)

    LaunchedEffect(Unit) {
        with(args) {
            viewModel.getCourseDecks(learningLanguageId, sourceLanguageId, courseId, deckId)
        }
    }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        DeckOverviewContent(viewModel.viewState.collectAsState().value)
    }
}

@Composable
private fun DeckOverviewContent(deck: Deck?) {
    val cellsNumber = if (deck?.isKanaDeck() == true) 5 else 2
    val cellsSpacer = if (deck?.isKanaDeck() == true) 8.dp else 16.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(cellsNumber),
        contentPadding = PaddingValues(16.dp),
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
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            deck.cards.forEach { card ->
                when (card) {
                    is Card.WordCard -> item { DeckOverviewWordCard(card) }
                    is Card.PhraseCard -> item { DeckOverviewPhraseCard(card) }
                    is Card.KanjiCard -> item { DeckOverviewKanjiCard(card) }
                    is Card.KanaCard -> {
                        item { DeckOverviewKanaCard(card) }
                        (0..<card.emptySpacesAfter()).forEach { item {} }
                    }

                    else -> {}

                }
            }
        } ?: item {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DeckOverviewWordCard(card: Card.WordCard, modifier: Modifier = Modifier) {
    DeckCard(modifier = modifier) {
        CardFront(card.front)
        Transcription(card.transcription)
        Divider(2.dp, Modifier.padding(vertical = 4.dp))
        Translation(card.translation)
    }
}

@Composable
fun DeckOverviewKanaCard(card: Card.KanaCard, modifier: Modifier = Modifier) {
    DeckCard(modifier = modifier, contentPadding = 0.dp) {
        CardFront(front = card.front, fontSizeMax = 48.sp)
        Transcription(
            transcription = "[${card.transcription}] ${card.mirror.front}",
            preformatted = true
        )
    }
}

@Composable
fun DeckOverviewKanjiCard(card: Card.KanjiCard, modifier: Modifier = Modifier) {
    DeckCard(modifier = modifier) {
        CardFront(card.front, modifier = Modifier.weight(1f).padding(horizontal = 24.dp)) {
            Reading(card.reading.on)
        }
        Transcription(card.transcription)
        Divider(2.dp, Modifier.padding(vertical = 4.dp))
        Translation(card.translation)
    }
}

@Composable
fun DeckOverviewPhraseCard(card: Card.PhraseCard, modifier: Modifier = Modifier) {
    DeckCard(modifier = modifier) {
        CardFront(card.front)
        Transcription(card.transcription)
        Divider(2.dp, Modifier.padding(vertical = 4.dp))
        Translation(card.translation)
    }
}