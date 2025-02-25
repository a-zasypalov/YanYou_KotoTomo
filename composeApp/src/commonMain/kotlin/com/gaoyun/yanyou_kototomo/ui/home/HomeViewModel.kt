package com.gaoyun.yanyou_kototomo.ui.home

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.ui_state.HomeScreenSection
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.domain.HomeScreenInteractor
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val interactor: HomeScreenInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<PersonalSpaceState?>(null)

    fun getSpaceState() = viewModelScope.launch {
        viewState.value = interactor.getPersonalSpaceState()
    }

    fun updateShowToReviewCards(show: Boolean) {
        interactor.toggleSectionVisibility(HomeScreenSection.CardsToReview, show)
        viewState.value = viewState.value?.copy(showToReviewCards = show)
    }

    fun updateShowNewCards(show: Boolean) {
        interactor.toggleSectionVisibility(HomeScreenSection.NewCards, show)
        viewState.value = viewState.value?.copy(showNewCards = show)
    }

    fun updateShowPausedCards(show: Boolean) {
        interactor.toggleSectionVisibility(HomeScreenSection.PausedCards, show)
        viewState.value = viewState.value?.copy(showPausedCards = show)
    }

    fun updateShowCompletedCards(show: Boolean) {
        interactor.toggleSectionVisibility(HomeScreenSection.CompletedCards, show)
        viewState.value = viewState.value?.copy(showCompletedCards = show)
    }
}