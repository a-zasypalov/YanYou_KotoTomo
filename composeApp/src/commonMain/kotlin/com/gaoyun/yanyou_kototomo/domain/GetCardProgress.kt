package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CardSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.data.remote.converters.toSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository

class GetCardProgress(
    private val repository: CardsAndProgressRepository,
) {

    fun getCardProgressPage(page: Int): List<CardSimpleDataEntryWithProgress> {
        val progresses = repository.getCardProgressPage(page)
        val cardDTOs = repository.getCards(progresses.map { it.cardId })

        return cardDTOs.mapNotNull { card ->
            progresses.find { it.cardId == card.id }?.let {
                card.toSimpleDataEntryWithProgress(it)
            }
        }
    }

}