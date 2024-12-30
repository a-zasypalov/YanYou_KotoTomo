package com.gaoyun.yanyou_kototomo.ui.settings.sections

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.domain.AllDataReset
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.util.AppIcon
import com.gaoyun.yanyou_kototomo.util.AppTheme
import com.gaoyun.yanyou_kototomo.util.ThemeChanger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class SettingsViewModel(
    private val themeChanger: ThemeChanger,
    private val preferences: Preferences,
    private val dataReset: AllDataReset,
) : BaseViewModel() {
    override val viewState = MutableStateFlow<SettingsViewState?>(null)

    fun getSettings() = viewModelScope.launch {

    }

    fun setAppIcon(icon: AppIcon) {
        themeChanger.activateIcon(icon)
    }

    fun setAppTheme(theme: AppTheme) {
        preferences.setString(PreferencesKeys.COLOR_THEME, theme.name)
        themeChanger.applyTheme()
    }

    fun resetAllData() = viewModelScope.launch {
        dataReset.resetApp()
    }
}

data class SettingsViewState(val appIcon: String)