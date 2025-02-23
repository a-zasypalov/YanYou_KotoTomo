package com.gaoyun.yanyou_kototomo.data.ui_state

import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress

sealed interface CardOverviewPart {
    val name: String
    val cards: kotlin.collections.List<Any>
    val type: CardCategoryType
    val isShown: Boolean
    val visibilityToggle: ((Boolean) -> Unit)?

    data class Grid(
        override val name: String,
        override val cards: kotlin.collections.List<CardWithProgress<*>>,
        override val type: CardCategoryType = CardCategoryType.New,
        override val isShown: Boolean = true,
        override val visibilityToggle: ((Boolean) -> Unit)? = null,
        val span: Int = 1,
    ) : CardOverviewPart

    data class List(
        override val name: String,
        override val cards: kotlin.collections.List<CardWithProgress.WithDeckInfo<*>>, //<-- with deck
        override val type: CardCategoryType = CardCategoryType.New,
        override val isShown: Boolean = true,
        override val visibilityToggle: ((Boolean) -> Unit)? = null,
    ) : CardOverviewPart
}