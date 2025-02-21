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
    internal val preferences: Preferences,
    internal val coursesRootComponentRepository: CoursesRootComponentRepository,
) {
    internal val courseDecks = MutableStateFlow<List<CourseDeck>?>(null)

    fun getLearningDecks(): List<CourseDeck> {
        val jsonString = preferences.getString(PreferencesKeys.LEARNING_DECKS, "[]")
        return try {
            val dtoList = Json.decodeFromString(ListSerializer(CourseDeckDTO.serializer()), jsonString)
            dtoList.map { it.toLocal() }
        } catch (e: SerializationException) {
            e.printStackTrace()
            emptyList() // Fallback to an empty list if deserialization fails
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

    suspend fun addLearningDeck(deckId: DeckId, decks: List<CourseDeck>) {
        val courseDecks = this.courseDecks.value ?: getCourseDecks()
        val deckToAdd = courseDecks.find { it.id == deckId }
        val decksToSave = (decks + deckToAdd).filterNotNull()
        saveLearningDecks(decksToSave)
    }

    suspend fun addBookmark(deckId: DeckId, decks: List<CourseDeck>) {
        val courseDecks = this.courseDecks.value ?: getCourseDecks()
        val deckToAdd = courseDecks.find { it.id == deckId }
        val decksToSave = (decks + deckToAdd).filterNotNull()

        saveBookmarkedDecks(decksToSave)
    }

    fun saveBookmarkedDecks(decks: List<CourseDeck>) {
        val jsonString = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), decks.map { it.toDTO() })
        preferences.setString(PreferencesKeys.BOOKMARKED_DECKS, jsonString)
    }

    fun saveLearningDecks(decks: List<CourseDeck>) {
        val jsonString = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), decks.map { it.toDTO() })
        preferences.setString(PreferencesKeys.LEARNING_DECKS, jsonString)
    }

    internal suspend fun getCourseDecks(): List<CourseDeck> {
        return coursesRootComponentRepository.getCoursesRoot(force = false)
            .toLocal().languages
            .flatMap { it.sourceLanguages.flatMap { s -> s.courses.flatMap { c -> c.decks } } }
            .also { this.courseDecks.value = it }
    }

}