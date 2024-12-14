package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetCoursesRoot(private val repository: CoursesRootComponentRepository) {
    suspend operator fun invoke(): Flow<RootStructure> = flowOf(repository.getCoursesRoot())
}