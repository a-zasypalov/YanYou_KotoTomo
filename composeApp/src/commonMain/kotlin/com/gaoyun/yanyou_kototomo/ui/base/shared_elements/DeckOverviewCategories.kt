package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewCard
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.emptySpacesAfter
import com.gaoyun.yanyou_kototomo.ui.personal_space.elements.PersonalAreaCardItem

fun LazyGridScope.DeckOverviewCategories(
    categories: List<CardOverviewPart.Grid>,
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

fun LazyListScope.DeckOverviewCategories(
    categories: List<CardOverviewPart.List>,
    onCardClick: (CardWithProgress<*>) -> Unit,
) {
    categories.forEach { (name, cards, _, isVisible, onToggle) ->
        if (cards.isNotEmpty()) {
            item {
                DeckOverviewCategoryHeader(
                    name = name,
                    isOpen = isVisible,
                    onOpenToggle = onToggle
                )
            }

            if (isVisible) {
                cards.forEach {
                    item(key = it.card.id.identifier) {
                        PersonalAreaCardItem(card = it, onCardClick)
                    }
                    (0..<it.card.emptySpacesAfter()).forEach { item {} }
                }
            }
        }
    }
}