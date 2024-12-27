package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckPlayer
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.CardDetailsView
import moe.tlaster.precompose.koin.koinViewModel


@Composable
fun DeckOverviewScreen(
    args: DeckScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = DeckOverviewViewModel::class)
    val cardDetailState = remember { mutableStateOf<CardWithProgress<*>?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState.value) {
        when (lifecycleState.value) {
            Lifecycle.State.RESUMED -> viewModel.getDeck(args)
            else -> {}
        }
    }

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
        actionButtons = { DeckOptionsMenu({ viewModel.resetDeck(args) }) }
    ) {
        DeckOverviewContent(
            viewState = viewModel.viewState.collectAsState().value,
            onCardClick = { cardToShow -> cardDetailState.value = cardToShow },
            onPlayDeckClick = { mode -> navigate(ToDeckPlayer(args.toPlayerArgs(mode))) },
            updateTranslationSettings = viewModel::updateTranslationSettings,
            updateTranscriptionSettings = viewModel::updateTranscriptionSettings,
            updateReadingSettings = viewModel::updateReadingSettings
        )
        CardDetailsView(cardDetailState, args.learningLanguageId) { cardDetailState.value = null }
    }
}

@Composable
private fun DeckOverviewContent(
    viewState: DeckOverviewState?,
    onCardClick: (CardWithProgress<*>) -> Unit,
    onPlayDeckClick: (PlayerMode) -> Unit,
    updateTranslationSettings: (Boolean) -> Unit,
    updateTranscriptionSettings: (Boolean) -> Unit,
    updateReadingSettings: (Boolean) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        viewState?.let {
            val deck = viewState.deck
            val cellsNumber = if (deck.isKanaDeck() == true) 5 else 2
            val cellsSpacer = if (deck.isKanaDeck() == true) 8.dp else 16.dp

            LazyVerticalGrid(
                columns = GridCells.Fixed(cellsNumber),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(cellsSpacer),
                horizontalArrangement = Arrangement.spacedBy(cellsSpacer),
                modifier = Modifier.fillMaxWidth()
            ) {
                item(span = { GridItemSpan(cellsNumber) }) {
                    DeckOverviewHeader(
                        viewState = viewState,
                        updateTranslationSettings = updateTranslationSettings,
                        updateTranscriptionSettings = updateTranscriptionSettings,
                        updateReadingSettings = updateReadingSettings
                    )
                }

                deck.cards.forEach { card ->
                    when (card.card) {
                        is Card.WordCard -> item {
                            DeckOverviewWordCard(
                                card = card.card,
                                showTranscription = viewState.settings.showTranscription,
                                showTranslation = viewState.settings.showTranslation,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.PhraseCard -> item {
                            DeckOverviewPhraseCard(
                                card = card.card,
                                showTranscription = viewState.settings.showTranscription,
                                showTranslation = viewState.settings.showTranslation,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.KanjiCard -> item {
                            DeckOverviewKanjiCard(
                                card = card.card,
                                showTranscription = viewState.settings.showTranscription,
                                showTranslation = viewState.settings.showTranslation,
                                showReading = viewState.settings.showReading,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.KanaCard -> {
                            item {
                                DeckOverviewKanaCard(
                                    card = card.card,
                                    showTranscription = viewState.settings.showTranscription,
                                    onClick = { onCardClick(card) }
                                )
                            }
                            (0..<card.card.emptySpacesAfter()).forEach { item {} }
                        }
                    }
                }

                item(span = { GridItemSpan(cellsNumber) }) {
                    Spacer(modifier = Modifier.height(96.dp))
                }

            }
            DeckOverviewActionButtons(viewState.cardsDueToReview, onPlayDeckClick)
        } ?: CircularProgressIndicator()
    }
}