package com.gaoyun.yanyou_kototomo.ui.statistics

import com.gaoyun.yanyou_kototomo.data.local.CardSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.data.local.QuizSessionWithSimpleDataEntryCards

data class StatisticsViewState(
    val sessions: Set<QuizSessionWithSimpleDataEntryCards>,
    val cardsProgress: Set<CardSimpleDataEntryWithProgress>,
)