package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi

class DeckRepository(
    private val api: DecksApi
) {

    suspend fun getDeck(
        learningLanguage: LanguageId,
        sourceLanguage: LanguageId,
        deckId: DeckId
    ): DeckDTO {
        val response =
            api.getDeck(learningLanguage.identifier, sourceLanguage.identifier, deckId.identifier)
        return response
    }
}