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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckQuizPlayer
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckReviewPlayer
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOptionsMenu
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewActionButtons
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewHeader
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewKanaDeck
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewMixedKanaDeck
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewNormalSegmentedDeck
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckPausedItemsSettingsView
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.deckOverviewKanaCategories
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.deckOverviewMixedKanaCategories
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.deckOverviewNormalSegmentedCategories
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DeckOverviewScreen(
    args: DeckScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<DeckOverviewViewModel>()

    val cardDetailState = remember { mutableStateOf<CardWithProgress<*>?>(null) }
    val cardDetailPausedState = remember { mutableStateOf<Boolean>(false) }
    val cardDetailCompletedState = remember { mutableStateOf<Boolean>(false) }

    val pausedCardsSettingState = remember { mutableStateOf<List<CardWithProgress<*>>?>(null) }
    val pausedCardsState = remember { mutableStateOf<List<CardWithProgress<*>>?>(null) }

    val lifecycleState = LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(lifecycleState.value) { if (lifecycleState.value == Lifecycle.State.RESUMED) viewModel.getDeck(args) }

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
                cardDetailCompletedState.value = cardToShow.isCompleted()
            },
            onPlayDeckClick = { mode ->
                when (mode) {
                    PlayerMode.Quiz -> navigate(ToDeckQuizPlayer(args.toPlayerDeckQuizArgs(PlayerBackRoute.Deck(args))))
                    PlayerMode.SpacialRepetition -> navigate(ToDeckReviewPlayer(args.toPlayerDeckReviewArgs(PlayerBackRoute.Deck(args))))
                    PlayerMode.MixedDeckReview -> {} //This shouldn't be activated here
                }
            },
            updateTranslationSettings = viewModel::updateTranslationSettings,
            updateTranscriptionSettings = viewModel::updateTranscriptionSettings,
            updateReadingSettings = viewModel::updateReadingSettings,
            updateBookmarkedState = viewModel::updateBookmarkedState,
            updateShowNewWords = viewModel::updateShowNewWords,
            updateShowNewPhrases = viewModel::updateShowNewPhrases,
            updateShowToReviewCards = viewModel::updateShowToReviewCards,
            updateShowPausedCards = viewModel::updateShowPausedCards,
            updateShowCompletedCards = viewModel::updateShowCompletedCards,
            updateShowKanjiCards = viewModel::updateShowKanjiCards,
            updateLearnedState = viewModel::updateLearnedState
        )
        CardDetailsView(
            cardState = cardDetailState,
            paused = cardDetailPausedState,
            completed = cardDetailCompletedState,
            languageId = args.learningLanguageId,
            onCardPause = viewModel::pauseCard,
            onCardComplete = viewModel::completeCard,
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
    updateShowNewWords: (Boolean) -> Unit,
    updateShowNewPhrases: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
    updateShowKanjiCards: (Boolean) -> Unit,
    updateLearnedState: (Boolean) -> Unit,
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

        val deckOverviewNormalSegmentedCategories = deckOverviewNormalSegmentedCategories(
            viewState = viewState,
            updateShowNewWords = updateShowNewWords,
            updateShowNewPhrases = updateShowNewPhrases,
            updateShowToReviewCards = updateShowToReviewCards,
            updateShowPausedCards = updateShowPausedCards,
            updateShowKanji = updateShowKanjiCards,
            updateShowCompletedCards = updateShowCompletedCards
        )
        val deckOverviewMixedKanaCategories = deckOverviewMixedKanaCategories(
            viewState = viewState,
            updateShowNewWords = updateShowNewWords,
            updateShowToReviewCards = updateShowToReviewCards,
            updateShowPausedCards = updateShowPausedCards,
            updateShowCompletedCards = updateShowCompletedCards,
        )
        val deckOverviewKanaCategories = deckOverviewKanaCategories(viewState)

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
                        categories = deckOverviewKanaCategories,
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, false) }
                    )

                    viewState.deckId.isMixedKanaDeck() -> DeckOverviewMixedKanaDeck(
                        categories = deckOverviewMixedKanaCategories,
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, viewState.pausedCards.contains(it)) },
                    )

                    else -> DeckOverviewNormalSegmentedDeck(
                        categories = deckOverviewNormalSegmentedCategories,
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, viewState.isCardPaused(it.card.id)) },
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