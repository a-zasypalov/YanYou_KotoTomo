package com.gaoyun.yanyou_kototomo.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel() : ViewModel() {
    abstract val viewState: MutableStateFlow<*>
}