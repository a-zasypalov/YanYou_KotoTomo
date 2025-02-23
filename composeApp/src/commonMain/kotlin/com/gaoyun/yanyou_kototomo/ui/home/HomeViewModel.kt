package com.gaoyun.yanyou_kototomo.ui.home

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.ui_state.HomeState
import com.gaoyun.yanyou_kototomo.domain.GetHomeState
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeState: GetHomeState,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<HomeState?>(null)

    fun getHomeState() = viewModelScope.launch { viewState.value = getHomeState.getHomeState() }

}