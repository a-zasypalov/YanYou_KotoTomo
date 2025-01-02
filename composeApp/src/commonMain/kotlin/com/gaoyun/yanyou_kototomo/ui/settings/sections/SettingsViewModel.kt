package com.gaoyun.yanyou_kototomo.ui.settings.sections

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.domain.AllDataReset
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
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
    private val getCoursesRoot: GetCoursesRoot,
) : BaseViewModel() {
    override val viewState = MutableStateFlow<SettingsViewState?>(null)

    fun getSettings() = viewModelScope.launch {
        viewState.value = SettingsViewState(getCoursesRoot.getCourseLanguages(), getPrimaryLanguageId())
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

    fun reloadCourses() = viewModelScope.launch {
        getCoursesRoot.getCourses(force = true)
    }

    private fun getPrimaryLanguageId(): LanguageId = LanguageId(preferences.getString(PreferencesKeys.PRIMARY_LANGUAGE_ID, "cn"))
    fun setPrimaryLanguageId(id: LanguageId) {
        preferences.setString(PreferencesKeys.PRIMARY_LANGUAGE_ID, id.identifier)
        viewState.value = viewState.value?.copy(primaryLanguageId = id)
    }
}

data class SettingsViewState(val availableLanguages: List<LanguageId>, val primaryLanguageId: LanguageId)