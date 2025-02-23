package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewCategory
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewState

fun LazyGridScope.DeckOverviewKanaDeck(
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    val gojuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Gojuon }
    val dakuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.DakuonHandakuon }
    val yoonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Yoon }

    val categories = listOf(
        CardOverviewCategory.GridOverview(name = "Gojuon", cards = gojuonCards, span = 3),
        CardOverviewCategory.GridOverview(name = "Dakuon and Handakuon", cards = dakuonCards, span = 3),
        CardOverviewCategory.GridOverview(name = "Yoon", cards = yoonCards, span = 5),
    )

    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}

fun LazyGridScope.DeckOverviewMixedKanaDeck(
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
    updateShowNewWords: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
) {
    val gojuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Gojuon }
    val dakuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.DakuonHandakuon }
    val yoonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Yoon }

    val showNewWords = viewState.settings.hiddenSections.contains(DeckSettings.Sections.NewWords).not()
    val showToReviewCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Review).not()
    val showPausedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Paused).not()
    val showCompletedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Completed).not()

    val categories = listOf(
        CardOverviewCategory.GridOverview(name = "Gojuon", cards = gojuonCards, span = 3),
        CardOverviewCategory.GridOverview(name = "Dakuon and Handakuon", cards = dakuonCards, span = 3),
        CardOverviewCategory.GridOverview(name = "Yoon", cards = yoonCards, span = 5),
        CardOverviewCategory.GridOverview(
            name = "New words",
            cards = viewState.newCards.words,
            type = CardCategoryType.New,
            isShown = showNewWords,
            visibilityToggle = updateShowNewWords,
            span = 5
        ),
        CardOverviewCategory.GridOverview(
            name = "To Review",
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards,
            span = 5
        ),
        CardOverviewCategory.GridOverview(
            name = "Paused",
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards,
            span = 5
        ),
        CardOverviewCategory.GridOverview(
            name = "✓ Completed",
            cards = viewState.completedCards,
            type = CardCategoryType.Completed,
            isShown = showCompletedCards,
            visibilityToggle = updateShowCompletedCards,
            span = 5
        )
    )

    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}


fun LazyGridScope.DeckOverviewNormalSegmentedDeck(
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
    updateShowNewWords: (Boolean) -> Unit,
    updateShowNewPhrases: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
    updateShowKanji: (Boolean) -> Unit,
) {
    val showNewKanji = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Kanji).not()
    val showNewWords = viewState.settings.hiddenSections.contains(DeckSettings.Sections.NewWords).not()
    val showNewPhrases = viewState.settings.hiddenSections.contains(DeckSettings.Sections.NewPhrases).not()
    val showToReviewCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Review).not()
    val showPausedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Paused).not()
    val showCompletedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Completed).not()

    val categories = listOf(
        CardOverviewCategory.GridOverview("New Kanji", viewState.newCards.kanji, CardCategoryType.New, showNewKanji, updateShowKanji),
        CardOverviewCategory.GridOverview("New words", viewState.newCards.words, CardCategoryType.New, showNewWords, updateShowNewWords),
        CardOverviewCategory.GridOverview(
            "New phrases",
            viewState.newCards.phrases,
            CardCategoryType.New,
            showNewPhrases,
            updateShowNewPhrases
        ),
        CardOverviewCategory.GridOverview(
            "To Review",
            viewState.cardsToReview,
            CardCategoryType.ToReview,
            showToReviewCards,
            updateShowToReviewCards
        ),
        CardOverviewCategory.GridOverview("Paused", viewState.pausedCards, CardCategoryType.Paused, showPausedCards, updateShowPausedCards),
        CardOverviewCategory.GridOverview(
            "✓ Completed",
            viewState.completedCards,
            CardCategoryType.Completed,
            showCompletedCards,
            updateShowCompletedCards
        )
    )

    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}

fun LazyGridScope.DeckOverviewCategories(
    categories: List<CardOverviewCategory.GridOverview>,
    cellsNumber: Int,
    settings: DeckSettings,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    categories.forEach { (name, cards, type, isVisible, onToggle, span) ->
        if (cards.isNotEmpty()) {
            item(span = { GridItemSpan(cellsNumber) }) {
                DeckOverviewCategoryHeader(
                    name = name,
                    isOpen = isVisible,
                    onOpenToggle = onToggle
                )
            }

            if (isVisible) {
                cards.forEach {
                    item(key = it.card.id.identifier, span = { GridItemSpan(span) }) {
                        DeckOverviewCard(
                            cardWithProgress = if (type == CardCategoryType.Paused) it.withoutProgress() else it,
                            settings = settings,
                            onCardClick = onCardClick,
                        )
                    }
                    (0..<it.card.emptySpacesAfter()).forEach { item {} }
                }
            }
        }
    }
}

enum class CardCategoryType { New, ToReview, Paused, Completed }

@Composable
fun DeckOverviewCategoryHeader(name: String, isOpen: Boolean, onOpenToggle: ((Boolean) -> Unit)?) {
    val expandIconAngle = animateFloatAsState(targetValue = if (isOpen) 180f else 0f)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, style = MaterialTheme.typography.headlineMedium)
            onOpenToggle?.let {
                IconButton(onClick = { onOpenToggle(!isOpen) }) {
                    Icon(Icons.Default.ExpandMore, "", modifier = Modifier.rotate(expandIconAngle.value))
                }
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
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                modifier = modifier
            )

        is Card.PhraseCard ->
            DeckOverviewPhraseCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )

        is Card.KanjiCard ->
            DeckOverviewKanjiCard(
                card = card,
                showTranscription = settings.showTranscription,
                showTranslation = settings.showTranslation,
                showReading = settings.showReading,
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )

        is Card.KanaCard -> {
            DeckOverviewKanaCard(
                card = card,
                showTranscription = settings.showTranscription,
                intervalInDays = cardWithProgress.progress?.interval,
                nextReviewDate = cardWithProgress.progress?.nextReview,
                completed = cardWithProgress.isCompleted(),
                onClick = { onCardClick(cardWithProgress) },
                modifier = modifier
            )
        }
    }
}