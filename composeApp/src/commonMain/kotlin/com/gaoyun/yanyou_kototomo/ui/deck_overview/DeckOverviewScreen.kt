@file:Suppress("FunctionName")

package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckPlayer
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import moe.tlaster.precompose.koin.koinViewModel


@Composable
fun DeckOverviewScreen(
    args: DeckScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = DeckOverviewViewModel::class)

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
) {
    viewState?.let {
        val cellsNumber = if (viewState.deckId.isKanaDeck() == true) 5 else 2
        val cellsSpacer = if (viewState.deckId.isKanaDeck() == true) 8.dp else 16.dp

        Box(modifier = Modifier.fillMaxSize()) {
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
                        updateReadingSettings = updateReadingSettings,
                        updateBookmarkedState = updateBookmarkedState,
                        updateLearnedState = updateLearnedState,
                    )
                }

                if (viewState.deckId.isKanaDeck()) {
                    DeckOverviewKanaDeck(viewState, { onCardClick(it, false) })
                } else {
                    DeckOverviewNormalSegmentedDeck(
                        viewState = viewState,
                        cellsNumber = cellsNumber,
                        onCardClick = { onCardClick(it, viewState.pausedCards.contains(it)) },
                        updateShowNewWords = updateShowNewWords,
                        updateShowNewPhrases = updateShowNewPhrases,
                        updateShowToReviewCards = updateShowToReviewCards,
                        updateShowPausedCards = updateShowPausedCards
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

fun LazyGridScope.DeckOverviewKanaDeck(
    viewState: DeckOverviewState,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    viewState.allCards.forEach {
        item {
            DeckOverviewCard(
                cardWithProgress = it,
                settings = viewState.settings,
                onCardClick = onCardClick,
            )
        }
        (0..<it.card.emptySpacesAfter()).forEach { item {} }
    }
}


fun LazyGridScope.DeckOverviewNormalSegmentedDeck(
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
    updateShowNewWords: (Boolean) -> Unit,
    updateShowNewPhrases: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
) {
    data class Category(
        val name: String,
        val type: CardCategoryType,
        val cards: List<CardWithProgress<*>>,
        val isShown: Boolean,
        val visibilityToggle: (Boolean) -> Unit,
    )

    val categories = listOf(
        Category("New words", CardCategoryType.New, viewState.newCards.words, viewState.settings.showNewWords, updateShowNewWords),
        Category("New phrases", CardCategoryType.New, viewState.newCards.phrases, viewState.settings.showNewPhrases, updateShowNewPhrases),
        Category(
            "To Review",
            CardCategoryType.ToReview,
            viewState.cardsToReview,
            viewState.settings.showToReviewCards,
            updateShowToReviewCards
        ),
        Category("Paused", CardCategoryType.Paused, viewState.pausedCards, viewState.settings.showPausedCards, updateShowPausedCards)
    )

    categories.forEach { (name, type, cards, isVisible, onToggle) ->
        if (cards.isNotEmpty()) {
            item(span = { GridItemSpan(cellsNumber) }) {
                DeckOverviewCategoryHeader(
                    name = name,
                    isOpen = isVisible,
                    onOpenToggle = onToggle
                )
            }

            if (isVisible) {
                items(cards, key = { it.card.id.identifier }) {
                    DeckOverviewCard(
                        cardWithProgress = if (type == CardCategoryType.Paused) it.copy(progress = null) else it,
                        settings = viewState.settings,
                        onCardClick = onCardClick,
                    )
                }
            }
        }
    }
}

enum class CardCategoryType { New, ToReview, Paused }

@Composable
fun DeckOverviewCategoryHeader(name: String, isOpen: Boolean, onOpenToggle: (Boolean) -> Unit) {
    val expandIconAngle = animateFloatAsState(targetValue = if (isOpen) 180f else 0f)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = { onOpenToggle(!isOpen) }) {
                Icon(Icons.Default.ExpandMore, "", modifier = Modifier.rotate(expandIconAngle.value))
            }
        }
        Divider(1.dp, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun DeckOverviewCard(
    cardWithProgress: CardWithProgress<*>,
    settings: DeckSettings,
    onCardClick: (CardWithProgress<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val card = cardWithProgress.card
    when (card) {
        is Card.WordCard ->
            DeckOverviewWordCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                onClick = { onCardClick(cardWithProgress) },
                nextReviewDate = cardWithProgress.progress?.nextReview,
                modifier = modifier
            )

        is Card.PhraseCard ->
            DeckOverviewPhraseCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )

        is Card.KanjiCard ->
            DeckOverviewKanjiCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                showReading = settings.showReading,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )

        is Card.KanaCard -> {
            DeckOverviewKanaCard(
                card = card,
                showTranscription = settings.showTranscription,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )
        }
    }
}