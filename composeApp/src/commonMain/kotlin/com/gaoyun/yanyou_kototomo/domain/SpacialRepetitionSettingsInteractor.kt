package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.SPACIAL_REPETITION_EASE_FACTOR
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.SPACIAL_REPETITION_EASY_ANSWER_WEIGHT
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.SPACIAL_REPETITION_HARD_ANSWER_WEIGHT
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys.SPACIAL_REPETITION_INTERVAL_BASE

class SpacialRepetitionSettingsInteractor(private val preferences: Preferences) {

    companion object {
        const val DEFAULT_EASE_FACTOR = 2.0f
        const val DEFAULT_INTERVAL_BASE = 1.5f
        const val DEFAULT_EASY_ANSWER_WEIGHT = 0.2f
        const val DEFAULT_GOOD_ANSWER_WEIGHT = 0.1f
        const val DEFAULT_HARD_ANSWER_WEIGHT = 0.2f
    }

    fun easeFactor(): Float = preferences.getFloat(SPACIAL_REPETITION_EASE_FACTOR, DEFAULT_EASE_FACTOR)
    fun setEaseFactor(factor: Float) = preferences.setFloat(SPACIAL_REPETITION_EASE_FACTOR, factor)

    fun intervalBase(): Float = preferences.getFloat(SPACIAL_REPETITION_INTERVAL_BASE, DEFAULT_INTERVAL_BASE)
    fun setIntervalBase(base: Float) = preferences.setFloat(SPACIAL_REPETITION_INTERVAL_BASE, base)

    fun easyAnswerWeight(): Float = preferences.getFloat(SPACIAL_REPETITION_EASY_ANSWER_WEIGHT, DEFAULT_EASY_ANSWER_WEIGHT)
    fun setEasyAnswerWeight(weight: Float) = preferences.setFloat(SPACIAL_REPETITION_EASY_ANSWER_WEIGHT, weight)

    fun goodAnswerWeight(): Float = preferences.getFloat(SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT, DEFAULT_GOOD_ANSWER_WEIGHT)
    fun setGoodAnswerWeight(weight: Float) = preferences.setFloat(SPACIAL_REPETITION_GOOD_ANSWER_WEIGHT, weight)

    fun hardAnswerWeight(): Float = preferences.getFloat(SPACIAL_REPETITION_HARD_ANSWER_WEIGHT, DEFAULT_HARD_ANSWER_WEIGHT)
    fun setHardAnswerWeight(weight: Float) = preferences.setFloat(SPACIAL_REPETITION_HARD_ANSWER_WEIGHT, weight)
}