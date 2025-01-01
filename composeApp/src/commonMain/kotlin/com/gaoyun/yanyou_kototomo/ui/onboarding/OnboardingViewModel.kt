package com.gaoyun.yanyou_kototomo.ui.onboarding

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OnboardingViewModel() : BaseViewModel() {
    override val viewState = MutableStateFlow("")

    fun onPrimaryLanguageChosen(id: LanguageId) {

    }

    fun finishOnboarding() {

    }
}