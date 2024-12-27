package com.gaoyun.yanyou_kototomo.ui.course_decks

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.course.Course
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class CourseDecksViewModel(
    private val getCoursesRoot: GetCoursesRoot,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<Course?>(null)

    fun getCourseDecks(courseId: CourseId) = viewModelScope.launch {
        val result = getCoursesRoot.getCourseDecks(courseId)
        viewState.value = result
    }

}