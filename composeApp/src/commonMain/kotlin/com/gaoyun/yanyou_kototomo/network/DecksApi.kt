package com.gaoyun.yanyou_kototomo.network

import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.DeckUpdatesDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class DecksApi(private val client: HttpClient) {
    companion object {
        const val API_VERSION = "v1"
    }

    internal suspend fun getCoursesRootComponent(): RootStructureDTO {
        val responseString: String = client.requestAndCatch {
            get("${YanYouKotoTomoApi.GITHUB_ENDPOINT}/app_config/$API_VERSION/root.json").body()
        }
        return Json.decodeFromString(responseString)
    }

    internal suspend fun getDeck(
        learningLanguageId: String,
        sourceLanguage: String,
        deckId: String,
    ): DeckDTO? {
        val responseString: String = client.requestAndCatch {
            get("${YanYouKotoTomoApi.GITHUB_ENDPOINT}/app_config/$API_VERSION/cards/${learningLanguageId}/${sourceLanguage}/${deckId}.json").body()
        }
        return try {
            Json.decodeFromString(responseString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    internal suspend fun getDeckUpdates(): DeckUpdatesDTO? {
        val responseString: String = client.requestAndCatch {
            get("${YanYouKotoTomoApi.GITHUB_ENDPOINT}/app_config/$API_VERSION/deck_updates.json").body()
        }
        return try {
            Json.decodeFromString(responseString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}