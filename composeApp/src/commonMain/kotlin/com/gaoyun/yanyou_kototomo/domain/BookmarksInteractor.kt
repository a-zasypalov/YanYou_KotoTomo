package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.course.Course
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.LEARNING_DECKS
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
    internal val repository: CoursesRootComponentRepository,
) {
    internal val courseDecks = MutableStateFlow<List<CourseDeck>?>(null)

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

    fun getLearningCourseId(): CourseId? {
        return preferences.getString(PreferencesKeys.LEARNING_LANGUAGE)?.let { CourseId(it) }
    }

    suspend fun getLearningCourse(): Course? {
        return getLearningCourseId()?.let { repository.getCourse(it).toLocal() }
    }

    fun saveLearningCourse(courseId: CourseId?) = courseId?.let {
        preferences.setString(PreferencesKeys.LEARNING_LANGUAGE, it.identifier)
    } ?: preferences.remove(PreferencesKeys.LEARNING_LANGUAGE)

    fun getLearningDecks(): List<CourseDeck> {
        val jsonString = preferences.getString(LEARNING_DECKS, "[]")
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

    fun saveLearningDecks(decks: List<CourseDeck>) {
        val jsonString = Json.encodeToString(ListSerializer(CourseDeckDTO.serializer()), decks.map { it.toDTO() })
        preferences.setString(LEARNING_DECKS, jsonString)
    }

    internal suspend fun getCourseDecks(): List<CourseDeck> {
        return coursesRootComponentRepository.getCoursesRoot(force = false)
            .toLocal().languages
            .flatMap { it.sourceLanguages.flatMap { s -> s.courses.flatMap { c -> c.decks } } }
            .also { this.courseDecks.value = it }
    }

}