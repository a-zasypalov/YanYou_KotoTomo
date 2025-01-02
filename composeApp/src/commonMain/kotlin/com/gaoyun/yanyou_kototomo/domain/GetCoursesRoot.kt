package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository

class GetCoursesRoot(
    private val repository: CoursesRootComponentRepository,
    private val preferences: Preferences,
    private val bookmarksInteractor: BookmarksInteractor,
) {
    suspend fun getCourses(force: Boolean = false): RootStructure {
        val primaryLanguageId = preferences.getString(PreferencesKeys.PRIMARY_LANGUAGE_ID, "cn")
        return repository.getCoursesRoot(force).toLocal().let {
            val responseDeckIds = it.languages.flatMap { it.sourceLanguages }.flatMap { it.courses }.flatMap { it.decks }.map { it.id }

            bookmarksInteractor.getBookmarkedDecks().filter { responseDeckIds.contains(it.id) }
                .let { bookmarksInteractor.saveBookmarkedDecks(it) }
            bookmarksInteractor.getLearningDeck()?.takeIf { !responseDeckIds.contains(it.id) }
                ?.let { bookmarksInteractor.setLearningDeckId(null) }

            it.copy(languages = it.languages.sortedBy { language -> if (language.id.identifier == primaryLanguageId) 0 else 1 })
        }
    }

    suspend fun getCourseLanguages(): List<LanguageId> = repository.getCoursesRoot(false).toLocal().languages.map { it.id }

    suspend fun getCourseDecks(courseId: CourseId) = repository.getCourse(courseId).toLocal()
}