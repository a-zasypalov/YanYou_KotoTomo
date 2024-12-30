package com.gaoyun.yanyou_kototomo.ui.settings.sections

import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.util.AppIcon
import com.gaoyun.yanyou_kototomo.util.ThemeChanger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class SettingsViewModel(
    private val themeChanger: ThemeChanger,
) : BaseViewModel() {
    override val viewState = MutableStateFlow<SettingsViewState?>(null)

    fun getSettings() = viewModelScope.launch {

    }

    fun setAppIcon(icon: AppIcon) {
        themeChanger.activateIcon(icon)
    }
}

data class SettingsViewState(val appIcon: String)