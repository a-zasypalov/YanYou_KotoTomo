package com.gaoyun.yanyou_kototomo.ui.home

import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel(
    private val getCoursesRoot: GetCoursesRoot,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<RootStructure?>(null)

}