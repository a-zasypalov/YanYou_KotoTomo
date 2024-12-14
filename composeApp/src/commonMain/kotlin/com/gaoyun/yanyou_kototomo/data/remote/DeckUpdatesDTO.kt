package com.gaoyun.yanyou_kototomo.data.remote

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeckUpdatesDTO(
    @SerialName("updates")
    val updates: List<DeckUpdateDTO>
)

@Serializable
data class DeckUpdateDTO(
    @SerialName("deck_id")
    val deckId: String,
    @SerialName("updated")
    val updated: LocalDate
)