package com.gaoyun.yanyou_kototomo.ui.courses

import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class CoursesViewModel(
    private val getCoursesRoot: GetCoursesRoot,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<RootStructure?>(null)

    fun getRootComponent() = viewModelScope.launch {
        val result = getCoursesRoot.getCourses()
        viewState.value = result
    }

}