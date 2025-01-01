package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys

class OnboardingInteractor(private val preferences: Preferences) {

    fun getOnboardingIsShown(): Boolean {
        return preferences.getBoolean(PreferencesKeys.ONBOARDING_IS_SHOWN, false)
    }

    fun setOnboardingIsShown() {
        preferences.setBoolean(PreferencesKeys.ONBOARDING_IS_SHOWN, true)
    }

    fun setPrimaryLanguage(id: LanguageId) {
        preferences.setString(PreferencesKeys.PRIMARY_LANGUAGE_ID, id.identifier)
    }

}