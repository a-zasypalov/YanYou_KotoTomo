package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.data.remote.converters.toLocal
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository

class GetCoursesRoot(private val repository: CoursesRootComponentRepository) {
    suspend fun getCourses(): RootStructure = repository.getCoursesRoot().toLocal()
    suspend fun getCourseDecks(courseId: CourseId) = repository.getCourse(courseId).toLocal()
}