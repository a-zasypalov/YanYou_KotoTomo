package com.gaoyun.yanyou_kototomo.ui.base.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckQuizArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenMixedDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsListMode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
data class CourseScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
)

@Serializable
data class DeckScreenArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
) {
    fun toPlayerDeckReviewArgs(backToRoute: PlayerBackRoute): PlayerScreenDeckReviewArgs {
        return PlayerScreenDeckReviewArgs(
            learningLanguageId = learningLanguageId,
            sourceLanguageId = sourceLanguageId,
            courseId = courseId,
            deckIds = listOf(deckId),
            backToRoute = backToRoute
        )
    }

    fun toPlayerDeckQuizArgs(backToRoute: PlayerBackRoute): PlayerScreenDeckQuizArgs {
        return PlayerScreenDeckQuizArgs(
            learningLanguageId = learningLanguageId,
            sourceLanguageId = sourceLanguageId,
            courseId = courseId,
            deckIds = listOf(deckId),
            backToRoute = backToRoute
        )
    }
}

@Serializable
enum class SettingsSections {
    AppIcon, ColorTheme, AboutApp, SpacialRepetition
}

@Serializable
data class SettingsSectionsArgs(val section: SettingsSections)

@Serializable
data class StatisticsModeArgs(val mode: StatisticsListMode)

@Serializable
data class QuizSessionSummaryArgs(
    val learningLanguageId: LanguageId,
    val sourceLanguageId: LanguageId,
    val courseId: CourseId,
    val deckId: DeckId,
    val backToRoute: PlayerBackRoute,
    val sessionId: QuizSessionId,
)

inline fun <reified T> serializableNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
): NavType<T> = object : NavType<T>(isNullableAllowed) {
    private val innerSerializer: KSerializer<T> = when (T::class) {
        List::class -> ListSerializer(DeckId.serializer()) as KSerializer<T> // Special case for List<DeckId>
        else -> serializer()
    }

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { stringValue ->
            try {
                json.decodeFromString(innerSerializer, stringValue)
            } catch (e: SerializationException) {
                throw IllegalArgumentException("Failed to deserialize ${T::class.simpleName}", e)
            }
        }
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        try {
            bundle.putString(key, json.encodeToString(innerSerializer, value))
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Failed to serialize ${T::class.simpleName}", e)
        }
    }

    override fun parseValue(value: String): T {
        return try {
            json.decodeFromString(innerSerializer, value)
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Failed to parse ${T::class.simpleName}", e)
        }
    }

    override fun serializeAsValue(value: T): String {
        return try {
            json.encodeToString(innerSerializer, value)
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Failed to serialize ${T::class.simpleName}", e)
        }
    }
}

val appTypeMap: Map<KType, NavType<*>> = mapOf(
    typeOf<CourseScreenArgs>() to serializableNavType<CourseScreenArgs>(),
    typeOf<DeckScreenArgs>() to serializableNavType<DeckScreenArgs>(),
    typeOf<PlayerScreenDeckReviewArgs>() to serializableNavType<PlayerScreenDeckReviewArgs>(),
    typeOf<PlayerScreenMixedDeckReviewArgs>() to serializableNavType<PlayerScreenMixedDeckReviewArgs>(),
    typeOf<PlayerScreenDeckQuizArgs>() to serializableNavType<PlayerScreenDeckQuizArgs>(),
    typeOf<QuizSessionSummaryArgs>() to serializableNavType<QuizSessionSummaryArgs>(),
    typeOf<StatisticsModeArgs>() to serializableNavType<StatisticsModeArgs>(),
    typeOf<SettingsSectionsArgs>() to serializableNavType<SettingsSectionsArgs>(),
    typeOf<LanguageId>() to serializableNavType<LanguageId>(),
    typeOf<CourseId>() to serializableNavType<CourseId>(),
    typeOf<DeckId>() to serializableNavType<DeckId>(),
    typeOf<QuizSessionId>() to serializableNavType<QuizSessionId>(),
    typeOf<SettingsSections>() to serializableNavType<SettingsSections>(),
    typeOf<PlayerBackRoute>() to serializableNavType<PlayerBackRoute>(),
    typeOf<PlayerMode>() to serializableNavType<PlayerMode>(),
    typeOf<StatisticsListMode>() to serializableNavType<StatisticsListMode>(),
    typeOf<List<DeckId>>() to serializableNavType<List<DeckId>>(),
)