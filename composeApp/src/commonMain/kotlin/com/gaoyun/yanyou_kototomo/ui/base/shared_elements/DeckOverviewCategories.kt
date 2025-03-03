package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckSettings
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.DeckOverviewCard
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.emptySpacesAfter

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
    modifier: Modifier = Modifier,
    onCardClick: (DeckId, CardWithProgress<*>, LanguageId, Boolean) -> Unit,
) {
    val pausedCards = categories.find { it.type == CardCategoryType.Paused }?.cards ?: listOf()
    categories.forEach { (name, cards, _, isVisible, onToggle) ->
        if (cards.isNotEmpty()) {
            item {
                DeckOverviewCategoryHeader(
                    name = name,
                    isOpen = isVisible,
                    onOpenToggle = onToggle,
                    modifier = modifier
                )
            }

            if (isVisible) {
                cards.forEach {
                    item(key = it.card.id.identifier) {
                        PersonalAreaCardItem(
                            card = it,
                            modifier = modifier,
                            onCardClick = { card, languageId ->
                                val paused = pausedCards.find { it.card.id == card.card.id } != null
                                onCardClick(it.deckId, card, languageId, paused)
                            }
                        )
                    }
                }
            }
        }
    }
}