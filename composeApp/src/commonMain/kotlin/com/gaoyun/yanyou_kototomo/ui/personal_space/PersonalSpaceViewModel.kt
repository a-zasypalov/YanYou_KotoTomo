package com.gaoyun.yanyou_kototomo.ui.personal_space

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.domain.PersonalSpaceInteractor
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonalSpaceViewModel(
    private val interactor: PersonalSpaceInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<PersonalSpaceState?>(null)

    fun getSpaceState() = viewModelScope.launch {
        viewState.value = interactor.getPersonalSpaceState()
    }

}