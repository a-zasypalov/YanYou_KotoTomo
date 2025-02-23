package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.lazy.grid.LazyGridScope
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckOverviewCategories
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
        CardOverviewPart.Grid(name = "Gojuon", cards = gojuonCards, span = 3),
        CardOverviewPart.Grid(name = "Dakuon and Handakuon", cards = dakuonCards, span = 3),
        CardOverviewPart.Grid(name = "Yoon", cards = yoonCards, span = 5),
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
        CardOverviewPart.Grid(name = "Gojuon", cards = gojuonCards, span = 3),
        CardOverviewPart.Grid(name = "Dakuon and Handakuon", cards = dakuonCards, span = 3),
        CardOverviewPart.Grid(name = "Yoon", cards = yoonCards, span = 5),
        CardOverviewPart.Grid(
            name = "New words",
            cards = viewState.newCards.words,
            type = CardCategoryType.New,
            isShown = showNewWords,
            visibilityToggle = updateShowNewWords,
            span = 5
        ),
        CardOverviewPart.Grid(
            name = "To Review",
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards,
            span = 5
        ),
        CardOverviewPart.Grid(
            name = "Paused",
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards,
            span = 5
        ),
        CardOverviewPart.Grid(
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
        CardOverviewPart.Grid(
            name = "New Kanji",
            cards = viewState.newCards.kanji,
            type = CardCategoryType.New,
            isShown = showNewKanji,
            visibilityToggle = updateShowKanji
        ),
        CardOverviewPart.Grid(
            name = "New words",
            cards = viewState.newCards.words,
            type = CardCategoryType.New,
            isShown = showNewWords,
            visibilityToggle = updateShowNewWords
        ),
        CardOverviewPart.Grid(
            name = "New phrases",
            cards = viewState.newCards.phrases,
            type = CardCategoryType.New,
            isShown = showNewPhrases,
            visibilityToggle = updateShowNewPhrases
        ),
        CardOverviewPart.Grid(
            name = "To Review",
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards
        ),
        CardOverviewPart.Grid(
            name = "Paused",
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards
        ),
        CardOverviewPart.Grid(
            name = "✓ Completed",
            cards = viewState.completedCards,
            type = CardCategoryType.Completed,
            isShown = showCompletedCards,
            visibilityToggle = updateShowCompletedCards
        )
    )

    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}