package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckOverviewCategories

fun LazyListScope.PersonalSegmentedArea(
    viewState: PersonalSpaceState,
    onCardClick: (DeckId, CardWithProgress<*>, LanguageId) -> Unit,
    updateShowNewCards: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
    showToReviewCards: Boolean,
    showNewCards: Boolean,
    showPausedCards: Boolean,
    showCompletedCards: Boolean,
) {
    val categories = listOf(
        CardOverviewPart.List(
            name = "To Review",
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards
        ),
        CardOverviewPart.List(
            name = "New",
            cards = viewState.newCards,
            type = CardCategoryType.New,
            isShown = showNewCards,
            visibilityToggle = updateShowNewCards
        ),
        CardOverviewPart.List(
            name = "Paused",
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards
        ),
        CardOverviewPart.List(
            name = "✓ Completed",
            cards = viewState.completedCards,
            type = CardCategoryType.Completed,
            isShown = showCompletedCards,
            visibilityToggle = updateShowCompletedCards
        )
    )

    DeckOverviewCategories(categories, Modifier.padding(horizontal = 16.dp), onCardClick)
}