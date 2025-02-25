package com.gaoyun.yanyou_kototomo.ui.course_decks

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.course.Course
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CourseDecksViewModel(
    private val getCoursesRoot: GetCoursesRoot,
    private val bookmarksInteractor: BookmarksInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<CourseDecksState?>(null)

    fun getCourseDecks(courseId: CourseId) = viewModelScope.launch {
        val result = getCoursesRoot.getCourse(courseId)
        val courseIsLearningState = bookmarksInteractor.getLearningCourseId() == result.id
        viewState.value = CourseDecksState(result, courseIsLearningState)
    }

    fun updateLearnedState(isLearned: Boolean) {
        bookmarksInteractor.saveLearningCourse(viewState.value?.course?.id ?: return)
        viewState.value = viewState.value?.copy(isLearned = isLearned)
    }

}

data class CourseDecksState(
    val course: Course,
    val isLearned: Boolean,
)