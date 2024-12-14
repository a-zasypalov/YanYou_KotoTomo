package com.gaoyun.yanyou_kototomo.data.local

data class RootStructure(
    val languages: List<LearningLanguage>
)

data class LearningLanguage(
    val id: LanguageId,
    val sourceLanguages: List<SourceLanguage>
)

data class SourceLanguage(
    val sourceLanguage: LanguageId,
    val courses: List<Course>
)