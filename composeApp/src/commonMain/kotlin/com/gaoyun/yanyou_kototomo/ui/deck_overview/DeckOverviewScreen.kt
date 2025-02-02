@file:Suppress("FunctionName")

package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckPlayer
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DeckOverviewScreen(
    args: DeckScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<DeckOverviewViewModel>()

    val cardDetailState = remember { mutableStateOf<CardWithProgress<*>?>(null) }
    val cardDetailPausedState = remember { mutableStateOf<Boolean>(false) }

    val pausedCardsSettingState = remember { mutableStateOf<List<CardWithProgress<*>>?>(null) }
    val pausedCardsState = remember { mutableStateOf<List<CardWithProgress<*>>?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState.value) {
        when (lifecycleState.value) {
            Lifecycle.State.RESUMED -> viewModel.getDeck(args)
            else -> {}
        }
    }

    val viewState = viewModel.viewState.collectAsState().value

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
        actionButtons = {
            DeckOptionsMenu(
                isKanaDeck = args.deckId.isKanaDeck(),
                onResetDeck = { viewModel.resetDeck(args) },
                onEditDeck = {
                    pausedCardsState.value = viewState?.pausedCards
                    pausedCardsSettingState.value = viewState?.allCards
                }
            )
        }
    ) {
        DeckOverviewContent(
            viewState = viewState,
            onCardClick = { cardToShow, paused ->
                cardDetailState.value = cardToShow
                cardDetailPausedState.value = paused
            },
            onPlayDeckClick = { mode -> navigate(ToDeckPlayer(args.toPlayerArgs(mode, PlayerBackRoute.Deck))) },
            updateTranslationSettings = viewModel::updateTranslationSettings,
            updateTranscriptionSettings = viewModel::updateTranscriptionSettings,
            updateReadingSettings = viewModel::updateReadingSettings,
            updateBookmarkedState = viewModel::updateBookmarkedState,
            updateLearnedState = viewModel::updateLearnedState,
            updateShowNewWords = viewModel::updateShowNewWords,
            updateShowNewPhrases = viewModel::updateShowNewPhrases,
            updateShowToReviewCards = viewModel::updateShowToReviewCards,
            updateShowPausedCards = viewModel::updateShowPausedCards,
            updateShowKanjiCards = viewModel::updateShowKanjiCards,
        )
        CardDetailsView(
            cardState = cardDetailState,
            paused = cardDetailPausedState,
            languageId = args.learningLanguageId,
            onCardPause = viewModel::pauseCard,
            onDismiss = { cardDetailState.value = null }
        )
        DeckPausedItemsSettingsView(
            allCards = pausedCardsSettingState,
            pausedCards = pausedCardsState,
            onCardPause = viewModel::pauseCard,
            onDismiss = { pausedCardsSettingState.value = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DeckOverviewContent(
    viewState: DeckOverviewState?,
    onCardClick: (CardWithProgress<*>, Boolean) -> Unit,
    onPlayDeckClick: (PlayerMode) -> Unit,
    updateTranslationSettings: (Boolean) -> Unit,
    updateTranscriptionSettings: (Boolean) -> Unit,
    updateReadingSettings: (Boolean) -> Unit,
    updateBookmarkedState: (Boolean) -> Unit,
    updateLearnedState: (Boolean) -> Unit,
    updateShowNewWords: (Boolean) -> Unit,
    updateShowNewPhrases: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowKanjiCards: (Boolean) -> Unit,
) {
    val state = rememberLazyGridState()
    viewState?.let {
        val cellsNumber = when {
            viewState.deckId.isKanaDeck() -> 15
            viewState.deckId.isMixedKanaDeck() -> 15
            else -> 2
        }
        val cellsSpacer = when {
            viewState.deckId.isKanaDeck() -> 8.dp
            viewState.deckId.isMixedKanaDeck() -> 8.dp
            else -> 16.dp
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                state = state,
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
                        updateReadingSettings = updateReadingSettings,
                        updateBookmarkedState = updateBookmarkedState,
                        updateLearnedState = updateLearnedState,
                    )
                }

                when {
                    viewState.deckId.isKanaDeck() -> DeckOverviewKanaDeck(
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, false) })

                    viewState.deckId.isMixedKanaDeck() -> DeckOverviewMixedKanaDeck(
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, viewState.pausedCards.contains(it)) },
                        updateShowNewWords = updateShowNewWords,
                        updateShowToReviewCards = updateShowToReviewCards,
                        updateShowPausedCards = updateShowPausedCards,
                    )

                    else -> DeckOverviewNormalSegmentedDeck(
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, viewState.pausedCards.contains(it)) },
                        updateShowNewWords = updateShowNewWords,
                        updateShowNewPhrases = updateShowNewPhrases,
                        updateShowToReviewCards = updateShowToReviewCards,
                        updateShowPausedCards = updateShowPausedCards,
                        updateShowKanji = updateShowKanjiCards
                    )
                }

                item(span = { GridItemSpan(cellsNumber) }) {
                    Spacer(modifier = Modifier.height(96.dp))
                }

            }
            DeckOverviewActionButtons(viewState.cardsDueToReview, onPlayDeckClick)
        }
    } ?: FullScreenLoader()
}