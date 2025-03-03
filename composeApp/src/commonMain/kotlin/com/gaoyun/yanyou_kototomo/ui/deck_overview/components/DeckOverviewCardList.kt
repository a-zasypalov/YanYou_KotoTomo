package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckOverviewCategories
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewState
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.completed_check
import yanyou_kototomo.composeapp.generated.resources.dakuon_handakuon
import yanyou_kototomo.composeapp.generated.resources.gojuon
import yanyou_kototomo.composeapp.generated.resources.new_kanji
import yanyou_kototomo.composeapp.generated.resources.new_phrases
import yanyou_kototomo.composeapp.generated.resources.new_words
import yanyou_kototomo.composeapp.generated.resources.paused
import yanyou_kototomo.composeapp.generated.resources.to_review
import yanyou_kototomo.composeapp.generated.resources.yoon

@Composable
internal fun deckOverviewKanaCategories(viewState: DeckOverviewState): List<CardOverviewPart.Grid> {
    val gojuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Gojuon }
    val dakuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.DakuonHandakuon }
    val yoonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Yoon }

    return listOf(
        CardOverviewPart.Grid(name = stringResource(Res.string.gojuon), cards = gojuonCards, span = 3),
        CardOverviewPart.Grid(name = stringResource(Res.string.dakuon_handakuon), cards = dakuonCards, span = 3),
        CardOverviewPart.Grid(name = stringResource(Res.string.yoon), cards = yoonCards, span = 5),
    )
}

fun LazyGridScope.DeckOverviewKanaDeck(
    categories: List<CardOverviewPart.Grid>,
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}

@Composable
internal fun deckOverviewMixedKanaCategories(
    viewState: DeckOverviewState,
    updateShowNewWords: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
): List<CardOverviewPart.Grid> {
    val gojuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Gojuon }
    val dakuonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.DakuonHandakuon }
    val yoonCards = viewState.allCards.filter { (it.card as? Card.KanaCard)?.set == Card.KanaCard.Set.Yoon }

    val showNewWords = viewState.settings.hiddenSections.contains(DeckSettings.Sections.NewWords).not()
    val showToReviewCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Review).not()
    val showPausedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Paused).not()
    val showCompletedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Completed).not()

    return listOf(
        CardOverviewPart.Grid(name = stringResource(Res.string.gojuon), cards = gojuonCards, span = 3),
        CardOverviewPart.Grid(name = stringResource(Res.string.dakuon_handakuon), cards = dakuonCards, span = 3),
        CardOverviewPart.Grid(name = stringResource(Res.string.yoon), cards = yoonCards, span = 5),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.new_words),
            cards = viewState.newCards.words,
            type = CardCategoryType.New,
            isShown = showNewWords,
            visibilityToggle = updateShowNewWords,
            span = 5
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.to_review),
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards,
            span = 5
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.paused),
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards,
            span = 5
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.completed_check),
            cards = viewState.completedCards,
            type = CardCategoryType.Completed,
            isShown = showCompletedCards,
            visibilityToggle = updateShowCompletedCards,
            span = 5
        )
    )
}

fun LazyGridScope.DeckOverviewMixedKanaDeck(
    categories: List<CardOverviewPart.Grid>,
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}

@Composable
internal fun deckOverviewNormalSegmentedCategories(
    viewState: DeckOverviewState,
    updateShowNewWords: (Boolean) -> Unit,
    updateShowNewPhrases: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
    updateShowKanji: (Boolean) -> Unit,
): List<CardOverviewPart.Grid> {
    val showNewKanji = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Kanji).not()
    val showNewWords = viewState.settings.hiddenSections.contains(DeckSettings.Sections.NewWords).not()
    val showNewPhrases = viewState.settings.hiddenSections.contains(DeckSettings.Sections.NewPhrases).not()
    val showToReviewCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Review).not()
    val showPausedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Paused).not()
    val showCompletedCards = viewState.settings.hiddenSections.contains(DeckSettings.Sections.Completed).not()

    return listOf(
        CardOverviewPart.Grid(
            name = stringResource(Res.string.new_kanji),
            cards = viewState.newCards.kanji,
            type = CardCategoryType.New,
            isShown = showNewKanji,
            visibilityToggle = updateShowKanji
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.new_words),
            cards = viewState.newCards.words,
            type = CardCategoryType.New,
            isShown = showNewWords,
            visibilityToggle = updateShowNewWords
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.new_phrases),
            cards = viewState.newCards.phrases,
            type = CardCategoryType.New,
            isShown = showNewPhrases,
            visibilityToggle = updateShowNewPhrases
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.to_review),
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.paused),
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards
        ),
        CardOverviewPart.Grid(
            name = stringResource(Res.string.completed_check),
            cards = viewState.completedCards,
            type = CardCategoryType.Completed,
            isShown = showCompletedCards,
            visibilityToggle = updateShowCompletedCards
        )
    )
}

fun LazyGridScope.DeckOverviewNormalSegmentedDeck(
    categories: List<CardOverviewPart.Grid>,
    viewState: DeckOverviewState,
    cellsNumber: Int,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    DeckOverviewCategories(categories, cellsNumber, viewState.settings, onCardClick)
}