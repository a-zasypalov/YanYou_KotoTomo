package com.gaoyun.yanyou_kototomo.ui.onboarding

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.domain.OnboardingInteractor
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OnboardingViewModel(private val interactor: OnboardingInteractor) : BaseViewModel() {
    override val viewState = MutableStateFlow(null)

    fun onPrimaryLanguageChosen(id: LanguageId) {
        interactor.setPrimaryLanguage(id)
    }

    fun finishOnboarding() {
        interactor.setOnboardingIsShown()
    }
}