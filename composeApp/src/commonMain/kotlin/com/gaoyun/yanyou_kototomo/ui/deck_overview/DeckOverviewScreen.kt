package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
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
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
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
    val cardDetailState = remember { mutableStateOf<Card?>(null) }

    LaunchedEffect(Unit) {
        with(args) {
            viewModel.getDeck(learningLanguageId, sourceLanguageId, courseId, deckId)
        }
    }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
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
    onCardClick: (Card) -> Unit,
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
                    Column(modifier = Modifier.fillMaxWidth()) {
                        AutoResizeText(
                            text = deck.name,
                            fontSizeRange = FontSizeRange(
                                max = MaterialTheme.typography.displayLarge.fontSize,
                                min = 24.sp
                            ),
                            maxLines = 1,
                            style = MaterialTheme.typography.displayLarge,
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (deck.isKanaDeck() == false) {
                                OutlinedIconToggleButton(
                                    checked = viewState.settings.showTranslation,
                                    onCheckedChange = updateTranslationSettings,
                                ) {
                                    Icon(Icons.Default.Translate, null)
                                }
                            }
                            OutlinedIconToggleButton(
                                checked = viewState.settings.showTranscription,
                                onCheckedChange = updateTranscriptionSettings,
                            ) {
                                Icon(Icons.Default.Subtitles, null)
                            }
                            if (deck.isJlptDeck() == true) {
                                OutlinedIconToggleButton(
                                    checked = viewState.settings.showReading,
                                    onCheckedChange = updateReadingSettings,
                                ) {
                                    Icon(Icons.Default.ViewColumn, null)
                                }
                            }
                        }
                    }
                }

                deck.cards.map { it.card }.forEach { card ->
                    when (card) {
                        is Card.WordCard -> item {
                            DeckOverviewWordCard(
                                card = card,
                                showTranscription = viewState.settings.showTranscription,
                                showTranslation = viewState.settings.showTranslation,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.PhraseCard -> item {
                            DeckOverviewPhraseCard(
                                card = card,
                                showTranscription = viewState.settings.showTranscription,
                                showTranslation = viewState.settings.showTranslation,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.KanjiCard -> item {
                            DeckOverviewKanjiCard(
                                card = card,
                                showTranscription = viewState.settings.showTranscription,
                                showTranslation = viewState.settings.showTranslation,
                                showReading = viewState.settings.showReading,
                                onClick = { onCardClick(card) }
                            )
                        }

                        is Card.KanaCard -> {
                            item {
                                DeckOverviewKanaCard(
                                    card = card,
                                    showTranscription = viewState.settings.showTranscription,
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

            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .padding(horizontal = 24.dp)
            ) {
                PrimaryElevatedButton(
                    text = "Review",
                    leadingIcon = Icons.Outlined.LocalLibrary,
                    onClick = { onPlayDeckClick(PlayerMode.SpacialRepetition) },
                    modifier = Modifier.weight(1f),
                )

                PrimaryElevatedButton(
                    text = "Quiz",
                    leadingIcon = Icons.Outlined.Quiz,
                    onClick = { onPlayDeckClick(PlayerMode.Quiz) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
        } ?: CircularProgressIndicator()
    }
}

@Composable
fun DeckOverviewWordCard(
    card: Card.WordCard,
    showTranslation: Boolean,
    showTranscription: Boolean,
    onClick: () -> Unit, modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier) {
        CardFront(card.front)
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}

@Composable
private fun DeckOverviewKanaCard(
    card: Card.KanaCard,
    showTranscription: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier, contentPadding = 0.dp) {
        CardFront(front = card.front, fontSizeMax = 48.sp)
        AnimatedVisibility(visible = showTranscription) {
            Transcription(
                transcription = "[${card.transcription}] ${card.mirror.front}",
                preformatted = true
            )
        }

    }
}

@Composable
private fun DeckOverviewKanjiCard(
    card: Card.KanjiCard,
    showTranslation: Boolean,
    showTranscription: Boolean,
    showReading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier) {
        CardFront(card.front, modifier = Modifier.weight(1f).padding(horizontal = 24.dp)) {
            this@DeckCard.AnimatedVisibility(visible = showReading, modifier = modifier.align(Alignment.CenterEnd)) {
                Reading(card.reading.on)
            }
        }
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}

@Composable
private fun DeckOverviewPhraseCard(
    card: Card.PhraseCard,
    showTranslation: Boolean,
    showTranscription: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckCard(onClick = onClick, modifier = modifier) {
        CardFront(card.front)
        AnimatedVisibility(visible = showTranscription) { Transcription(card.transcription) }
        AnimatedVisibility(visible = showTranscription && showTranslation) {
            Divider(2.dp, Modifier.padding(vertical = 4.dp))
        }
        AnimatedVisibility(visible = showTranslation) { Translation(card.translation) }
    }
}