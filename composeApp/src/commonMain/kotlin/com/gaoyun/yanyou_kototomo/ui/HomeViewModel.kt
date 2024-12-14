package com.gaoyun.yanyou_kototomo.ui

import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val getCoursesRoot: GetCoursesRoot
): ViewModel() {

    val viewState = MutableStateFlow("")

    fun getRootComponent() = viewModelScope.launch {
        val result = getCoursesRoot()
        viewState.value = result.toString()
    }

}