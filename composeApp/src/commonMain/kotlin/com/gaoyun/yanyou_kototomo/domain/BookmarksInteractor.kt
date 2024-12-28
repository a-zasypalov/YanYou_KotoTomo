package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.remote.CourseDeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toDTO
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class BookmarksInteractor(
    private val preferences: Preferences,
    private val coursesRootComponentRepository: CoursesRootComponentRepository,
) {
    private val courseDecks = MutableStateFlow<List<CourseDeck>?>(null)

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

    suspend fun setLearningDeckId(deckId: DeckId?) {
        if (deckId == null) {
            preferences.remove(PreferencesKeys.LEARNING_DECK)
        } else {
            (this.courseDecks.value ?: getCourseDecks()).find { it.id == deckId }?.let { deck ->
                val jsonString = Json.encodeToString(CourseDeckDTO.serializer(), deck.toDTO())
                preferences.setString(PreferencesKeys.LEARNING_DECK, jsonString)
            }
        }
    }

    suspend fun addDeck(deckId: DeckId, decks: List<CourseDeck>) {
        val courseDecks = this.courseDecks.value ?: getCourseDecks()
        val deckToAdd = courseDecks.find { it.id == deckId }
        val decksToSave = (decks + deckToAdd).filterNotNull()

        saveBookmarkedDecks(decksToSave)
    }

    fun saveBookmarkedDecks(decks: List<CourseDeck>) {
        val jsonString = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), decks.map { it.toDTO() })
        preferences.setString(PreferencesKeys.BOOKMARKED_DECKS, jsonString)
    }

    private suspend fun getCourseDecks(): List<CourseDeck> {
        return coursesRootComponentRepository.getCoursesRoot()
            .toLocal().languages
            .flatMap { it.sourceLanguages.flatMap { s -> s.courses.flatMap { c -> c.decks } } }
            .also { this.courseDecks.value = it }
    }

}