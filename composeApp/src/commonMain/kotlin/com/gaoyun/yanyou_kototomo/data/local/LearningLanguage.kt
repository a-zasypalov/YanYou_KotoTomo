package com.gaoyun.yanyou_kototomo.data.local

import com.gaoyun.yanyou_kototomo.data.local.course.Course

data class RootStructure(
    val languages: List<LearningLanguage>,
)

data class LearningLanguage(
    val id: LanguageId,
    val sourceLanguages: List<SourceLanguage>,
)

data class SourceLanguage(
    val id: LanguageId,
    val courses: List<Course>,
)