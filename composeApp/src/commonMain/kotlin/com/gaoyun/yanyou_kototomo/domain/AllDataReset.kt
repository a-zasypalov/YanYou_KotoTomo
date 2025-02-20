package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.BOOKMARKED_DECKS
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.LEARNING_DECK
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.UPDATES_COURSES_REFRESHED
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.UPDATES_STRUCTURE_REFRESHED
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase

class AllDataReset(private val preferences: Preferences, private val db: YanYouKotoTomoDatabase) {

    fun resetApp() {
        clearDatabase()
        clearPreferences()
    }

    internal fun clearDatabase() {
        db.card_progressQueries.deleteAll()
        db.coursesQueries.clearCache()
        db.deck_settingsQueries.deleteAll()
        db.decksQueries.deleteAll()
        db.quiz_sessionQueries.deleteAll()
        db.updatesQueries.deleteAll()
    }

    internal fun clearPreferences() {
        preferences.run {
            remove(UPDATES_STRUCTURE_REFRESHED)
            remove(UPDATES_COURSES_REFRESHED)
            remove(LEARNING_DECK)
            remove(BOOKMARKED_DECKS)
        }
    }

}