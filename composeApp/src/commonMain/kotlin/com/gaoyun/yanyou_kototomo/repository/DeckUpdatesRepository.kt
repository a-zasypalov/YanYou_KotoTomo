package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.remote.DeckUpdateDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.util.localDateTimeNow
import com.gaoyun.yanyou_kototomo.util.now
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

class DeckUpdatesRepository(
    private val api: DecksApi,
    private val db: YanYouKotoTomoDatabase,
    private val prefs: Preferences,
) {
    private val refreshTimeout = 2.days

    suspend fun shouldRefreshDeck(deckId: DeckId, deckVersion: Int): Boolean {
        val cacheTimeout = prefs.getString(PreferencesKeys.UPDATES_STRUCTURE_REFRESHED)
            ?.let { !shouldRefresh(LocalDateTime.parse(it)) } == true

        val newestVersion = if (cacheTimeout) {
            //Don't refresh if still cache timeout
            getLatestVersionForDeck(deckId)
        } else {
            //Check newest version from api
            fetchUpdatesFromApi()?.updates
                ?.find { it.deckId == deckId.identifier }?.version
                ?: Int.MAX_VALUE
        }

        return deckVersion < newestVersion
    }

    suspend fun shouldRefreshCourses(): Boolean {
        val cacheTimeout = prefs.getString(PreferencesKeys.UPDATES_STRUCTURE_REFRESHED)
            ?.let { !shouldRefresh(LocalDateTime.parse(it)) } == true

        //Don't refresh if still cache timeout
        if (cacheTimeout) return false

        //Last courses refresh. Should refresh if no data
        val lastUpdate = prefs.getString(PreferencesKeys.UPDATES_COURSES_REFRESHED)
            ?.let { LocalDateTime.parse(it) } ?: return true

        return check(lastUpdate)
    }

    private suspend fun check(lastUpdate: LocalDateTime): Boolean {
        return if (shouldRefresh(lastUpdate)) {
            //updated config, check if updated later than devices last update
            val updated = fetchUpdatesFromApi()?.coursesUpdated ?: return false
            updated > lastUpdate
        } else false
    }

    /**
     * Should update remote updates if more than 2 days passed
     */
    private fun shouldRefresh(lastUpdate: LocalDateTime) =
        lastUpdate < (now() - refreshTimeout).toLocalDateTime(TimeZone.currentSystemDefault())

    private suspend fun fetchUpdatesFromApi() = api.getDeckUpdates().also {
        prefs.setString(PreferencesKeys.UPDATES_STRUCTURE_REFRESHED, localDateTimeNow().toString())
        it?.updates?.let { insertDeckUpdates(it) }
    }

    private fun getLatestVersionForDeck(deckId: DeckId): Int {
        val res = db.updatesQueries.selectDeckById(deckId.identifier)
            .executeAsOneOrNull()
            ?.version
            ?.toInt() ?: 0
        return res
    }

    private fun insertDeckUpdates(dtoList: List<DeckUpdateDTO>) = dtoList.forEach { dto ->
        db.updatesQueries.updateDeck(deck_id = dto.deckId, version = dto.version.toLong())
    }
}