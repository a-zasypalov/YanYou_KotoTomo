package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.remote.RootStructureDTO
import com.gaoyun.yanyou_kototomo.network.DecksApi

class CoursesRootComponentRepository(
    private val api: DecksApi
) {

    suspend fun getCoursesRoot(): RootStructureDTO {
        val response = api.getCoursesRootComponent()
        return response
    }
}