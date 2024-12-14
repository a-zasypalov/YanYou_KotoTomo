package com.gaoyun.yanyou_kototomo.repository

import com.gaoyun.yanyou_kototomo.data.converters.toLocal
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.network.DecksApi

class CoursesRootComponentRepository(
    private val api: DecksApi
) {

    suspend fun getCoursesRoot(): RootStructure {
        val response = api.getCoursesRootComponent()
        return response.toLocal()
    }
}