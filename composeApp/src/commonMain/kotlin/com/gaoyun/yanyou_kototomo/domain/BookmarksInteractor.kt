package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class BookmarksInteractor(private val preferences: Preferences) {

    fun getLearningDeck(): CourseDeck? {
        val jsonString = preferences.getString(PreferencesKeys.LEARNING_DECK) ?: return null
        return try {
            val dto = Json.decodeFromString(CourseDeckDTO.serializer(), jsonString)
            dto.toLocal()
        } catch (e: SerializationException) {
            e.printStackTrace()
            null // Return null if deserialization fails
        }
    }

    fun getBookmarkedDecks(): List<CourseDeck> {
        val jsonString = preferences.getString(PreferencesKeys.BOOKMARKED_DECKS, "[]")
        return try {
            val dtoList = Json.decodeFromString(ListSerializer(CourseDeckDTO.serializer()), jsonString)
            dtoList.map { it.toLocal() }
        } catch (e: SerializationException) {
            e.printStackTrace()
            emptyList() // Fallback to an empty list if deserialization fails
        }
    }

    fun setLearningDeckId(deck: CourseDeck?) {
        if (deck == null) {
            preferences.remove(PreferencesKeys.LEARNING_DECK)
        } else {
            val jsonString = Json.encodeToString(CourseDeckDTO.serializer(), deck.toDTO())
            preferences.setString(PreferencesKeys.LEARNING_DECK, jsonString)
        }
    }

    fun setBookmarkedDeckIds(deckIds: List<CourseDeck>) {
        val jsonString = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), deckIds.map { it.toDTO() })
        preferences.setString(PreferencesKeys.BOOKMARKED_DECKS, jsonString)
    }

}