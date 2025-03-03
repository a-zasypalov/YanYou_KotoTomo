package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckOverviewCategories
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.completed_check
import yanyou_kototomo.composeapp.generated.resources.new
import yanyou_kototomo.composeapp.generated.resources.paused
import yanyou_kototomo.composeapp.generated.resources.to_review

@Composable
internal fun personalSegmentedCategories(
    viewState: PersonalSpaceState,
    updateShowNewCards: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
    showToReviewCards: Boolean,
    showNewCards: Boolean,
    showPausedCards: Boolean,
    showCompletedCards: Boolean,
) = listOf(
    CardOverviewPart.List(
        name = stringResource(Res.string.to_review),
        cards = viewState.cardsToReview,
        type = CardCategoryType.ToReview,
        isShown = showToReviewCards,
        visibilityToggle = updateShowToReviewCards
    ),
    CardOverviewPart.List(
        name = stringResource(Res.string.new),
        cards = viewState.newCards,
        type = CardCategoryType.New,
        isShown = showNewCards,
        visibilityToggle = updateShowNewCards
    ),
    CardOverviewPart.List(
        name = stringResource(Res.string.paused),
        cards = viewState.pausedCards,
        type = CardCategoryType.Paused,
        isShown = showPausedCards,
        visibilityToggle = updateShowPausedCards
    ),
    CardOverviewPart.List(
        name = stringResource(Res.string.completed_check),
        cards = viewState.completedCards,
        type = CardCategoryType.Completed,
        isShown = showCompletedCards,
        visibilityToggle = updateShowCompletedCards
    )
)

fun LazyListScope.PersonalSegmentedArea(
    categories: List<CardOverviewPart.List>,
    onCardClick: (DeckId, CardWithProgress<*>, LanguageId, Boolean) -> Unit,
) {
    DeckOverviewCategories(categories, Modifier.padding(horizontal = 16.dp), onCardClick)
}