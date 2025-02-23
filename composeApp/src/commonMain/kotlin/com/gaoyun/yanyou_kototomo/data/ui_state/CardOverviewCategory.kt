package com.gaoyun.yanyou_kototomo.data.ui_state

import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.CardCategoryType

sealed interface CardOverviewCategory {
    val name: String
    val cards: List<Any>
    val type: CardCategoryType
    val isShown: Boolean
    val visibilityToggle: ((Boolean) -> Unit)?

    data class GridOverview(
        override val name: String,
        override val cards: List<CardWithProgress<*>>,
        override val type: CardCategoryType = CardCategoryType.New,
        override val isShown: Boolean = true,
        override val visibilityToggle: ((Boolean) -> Unit)? = null,
        val span: Int = 1,
    ) : CardOverviewCategory

    data class ListOverview(
        override val name: String,
        override val cards: List<CardWithProgress.WithDeckInfo<*>>, //<-- with deck
        override val type: CardCategoryType = CardCategoryType.New,
        override val isShown: Boolean = true,
        override val visibilityToggle: ((Boolean) -> Unit)? = null,
    ) : CardOverviewCategory
}