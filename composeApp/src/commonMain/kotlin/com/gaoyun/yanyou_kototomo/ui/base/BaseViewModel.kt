package com.gaoyun.yanyou_kototomo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel() : ViewModel() {
    abstract val viewState: MutableStateFlow<*>

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}