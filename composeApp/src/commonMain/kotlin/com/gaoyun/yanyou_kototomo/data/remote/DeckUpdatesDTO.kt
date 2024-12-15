package com.gaoyun.yanyou_kototomo.data.remote

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeckUpdatesDTO(
    @SerialName("updates")
    val updates: List<DeckUpdateDTO>,
    @SerialName("courses_updated")
    val coursesUpdated: LocalDateTime
)

@Serializable
data class DeckUpdateDTO(
    @SerialName("deck_id")
    val deckId: String,
    @SerialName("version")
    val version: Int
)