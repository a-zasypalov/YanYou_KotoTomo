package com.gaoyun.yanyou_kototomo.ui.base

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

abstract class BaseViewModel() : ViewModel() {
    abstract val viewState: MutableStateFlow<*>

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}