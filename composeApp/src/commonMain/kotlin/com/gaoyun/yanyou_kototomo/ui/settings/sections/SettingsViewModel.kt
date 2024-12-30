package com.gaoyun.yanyou_kototomo.ui.settings.sections

import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.util.AppIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class SettingsViewModel(
) : BaseViewModel() {
    override val viewState = MutableStateFlow<SettingsViewState?>(null)

    fun getSettings() = viewModelScope.launch {

    }

    fun setAppIcon(icon: AppIcon) {

    }
}

data class SettingsViewState(val appIcon: String)