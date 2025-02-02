package com.gaoyun.yanyou_kototomo.ui.home

import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.domain.GetHomeState
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class HomeViewModel(
    private val getHomeState: GetHomeState,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<HomeState?>(null)

    fun getHomeState() = viewModelScope.launch { viewState.value = getHomeState.getHomeState() }

}