package com.gaoyun.yanyou_kototomo.ui.deck_overview

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
            onPlayDeckClick = { mode -> navigate(ToDeckPlayer(args.toPlayerArgs(mode, PlayerBackRoute.Deck))) },
            updateTranslationSettings = viewModel::updateTranslationSettings,
            updateTranscriptionSettings = viewModel::updateTranscriptionSettings,
            updateReadingSettings = viewModel::updateReadingSettings,
            updateBookmarkedState = viewModel::updateBookmarkedState,
            updateLearnedState = viewModel::updateLearnedState,
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
    updateBookmarkedState: (Boolean) -> Unit,
    updateLearnedState: (Boolean) -> Unit,
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

                if(viewState.newCards.isNotEmpty()) {
                    item(span = { GridItemSpan(cellsNumber) }) {
                        DeckOverviewCategoryHeader(name = "New", isOpen = true, onOpenToggle = {})
                    }

                    viewState.newCards.forEach {
                        item { DeckOverviewCard(it, viewState.settings, onCardClick) }
                        (0..<it.card.emptySpacesAfter()).forEach { item {} }
                    }
                }

                if(viewState.cardsToReview.isNotEmpty()) {
                    item(span = { GridItemSpan(cellsNumber) }) {
                        DeckOverviewCategoryHeader(name = "To Review", isOpen = true, onOpenToggle = {})
                    }

                    viewState.cardsToReview.forEach {
                        item { DeckOverviewCard(it, viewState.settings, onCardClick) }
                        (0..<it.card.emptySpacesAfter()).forEach { item {} }
                    }
                }

                if(viewState.pausedCards.isNotEmpty()) {
                    item(span = { GridItemSpan(cellsNumber) }) {
                        DeckOverviewCategoryHeader(name = "Paused", isOpen = true, onOpenToggle = {})
                    }

                    viewState.pausedCards.forEach {
                        item { DeckOverviewCard(it, viewState.settings, onCardClick) }
                        (0..<it.card.emptySpacesAfter()).forEach { item {} }
                    }
                }

                item(span = { GridItemSpan(cellsNumber) }) {
                    Spacer(modifier = Modifier.height(96.dp))
                }

            }
            DeckOverviewActionButtons(viewState.cardsDueToReview, onPlayDeckClick)
        }
    } ?: FullScreenLoader()
}

@Composable
fun DeckOverviewCategoryHeader(name: String, isOpen: Boolean, onOpenToggle: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = onOpenToggle) {
                if(isOpen) {
                    Icon(Icons.Default.ExpandMore, "")
                } else {
                    Icon(Icons.Default.ExpandMore, "")
                }
            }
        }
        Divider(1.dp, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun DeckOverviewCard(cardWithProgress: CardWithProgress<*>, settings: DeckSettings, onCardClick: (CardWithProgress<*>) -> Unit) {
    val card = cardWithProgress.card
    when (card) {
        is Card.WordCard ->
            DeckOverviewWordCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                onClick = { onCardClick(cardWithProgress) }
            )

        is Card.PhraseCard ->
            DeckOverviewPhraseCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                onClick = { onCardClick(cardWithProgress) }
            )

        is Card.KanjiCard ->
            DeckOverviewKanjiCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                showReading = settings.showReading,
                onClick = { onCardClick(cardWithProgress) }
            )

        is Card.KanaCard -> {
            DeckOverviewKanaCard(
                card = card,
                showTranscription = settings.showTranscription,
                onClick = { onCardClick(cardWithProgress) }
            )
        }
    }
}