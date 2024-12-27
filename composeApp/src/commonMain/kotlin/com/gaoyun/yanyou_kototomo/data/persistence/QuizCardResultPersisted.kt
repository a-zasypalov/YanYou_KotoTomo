package com.gaoyun.yanyou_kototomo.data.persistence

import kotlinx.serialization.Serializable

@Serializable
data class QuizCardResultPersisted(
    val cardId: String,
    val isCorrect: Boolean,
)