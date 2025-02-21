package com.gaoyun.yanyou_kototomo.data.persistence

object PreferencesKeys {
    const val UPDATES_STRUCTURE_REFRESHED = "UPDATES_STRUCTURE_REFRESHED"
    const val UPDATES_COURSES_REFRESHED = "UPDATES_COURSES_REFRESHED"
    const val LEARNING_DECKS = "LEARNING_DECKS"
    const val BOOKMARKED_DECKS = "BOOKMARKED_DECKS"
    const val COLOR_THEME = "COLOR_THEME"
    const val ONBOARDING_IS_SHOWN = "ONBOARDING_IS_SHOWN"
    const val PRIMARY_LANGUAGE_ID = "PRIMARY_LANGUAGE_ID"
    const val SPACIAL_REPETITION_EASE_FACTOR = "SPACIAL_REPETITION_EASE_FACTOR"
    const val SPACIAL_REPETITION_INTERVAL_BASE = "SPACIAL_REPETITION_INTERVAL_BASE"
    const val SPACIAL_REPETITION_EASY_ANSWER_WEIGHT = "SPACIAL_REPETITION_EASY_ANSWER_WEIGHT"
    const val SPACIAL_REPETITION_HARD_ANSWER_WEIGHT = "SPACIAL_REPETITION_HARD_ANSWER_WEIGHT"
    const val SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT = "SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT"
}

expect class Preferences(name: String? = null) {

    fun setInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
    fun getInt(key: String): Int?

    fun setFloat(key: String, value: Float)
    fun getFloat(key: String, defaultValue: Float): Float
    fun getFloat(key: String): Float?

    fun setLong(key: String, value: Long)
    fun getLong(key: String, defaultValue: Long): Long
    fun getLong(key: String): Long?

    fun setString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
    fun getString(key: String): String?

    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun getBoolean(key: String): Boolean?

    fun remove(key: String)
    fun clear()

    fun hasKey(key: String): Boolean
}