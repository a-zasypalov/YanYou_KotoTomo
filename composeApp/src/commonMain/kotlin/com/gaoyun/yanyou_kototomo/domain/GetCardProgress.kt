package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.card.CardSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.data.remote.converters.toSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository

class GetCardProgress(
    private val repository: CardsAndProgressRepository,
) {

    fun getCardProgressPage(page: Int): List<CardSimpleDataEntryWithProgress> {
        val progresses = repository.getCardProgressPage(page)
        val cardDTOs = repository.getCards(progresses.map { it.cardId })

        return cardDTOs.mapNotNull { cardWithDeckNames ->
            progresses.find { it.cardId == cardWithDeckNames.first.id }?.let {
                if (it.completed) return@mapNotNull null //Don't take completed cards
                cardWithDeckNames.first.toSimpleDataEntryWithProgress(it)
            }
        }
    }

}