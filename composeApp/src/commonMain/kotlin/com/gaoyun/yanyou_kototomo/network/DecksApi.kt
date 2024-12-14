package com.gaoyun.yanyou_kototomo.network

import com.gaoyun.yanyou_kototomo.data.remote.DeckDTO
import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class DecksApi(private val client: HttpClient) {

    internal suspend fun getCoursesRootComponent(): RootStructureDTO {
        val responseString: String = client.requestAndCatch {
            get("${YanYouKotoTomoApi.GITHUB_ENDPOINT}/app_config/root.json").body()
        }
        return Json.decodeFromString(responseString)
    }

    internal suspend fun getDeck(sourceLanguage: String, courseChapterId: String): DeckDTO {
        val responseString: String = client.requestAndCatch {
            get("${YanYouKotoTomoApi.GITHUB_ENDPOINT}/app_config/${sourceLanguage}/${courseChapterId}.json").body()
        }
        return Json.decodeFromString(responseString)
    }
}